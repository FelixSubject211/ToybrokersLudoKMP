package com.toybrokers.ludo.core.application

import com.toybrokers.ludo.core.domain.entities.GameState
import com.toybrokers.ludo.core.domain.entities.Player
import com.toybrokers.ludo.core.domain.entities.TurnStatus
import com.toybrokers.ludo.core.domain.events.GameEvent
import com.toybrokers.ludo.core.domain.interfaces.GameEventManager
import com.toybrokers.ludo.core.domain.interfaces.MoveCalculator
import com.toybrokers.ludo.core.domain.interfaces.Opponent
import com.toybrokers.ludo.di.Koin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.get


class DefaultOpponent(
    private val player: Player,
    private val gameEventManager: GameEventManager,
    private val moveCalculator: MoveCalculator = Koin.get()
): Opponent {
    private var job: Job? = null

    override fun start() {
        job = CoroutineScope(Dispatchers.Default).launch {
            gameEventManager.currentState()
                .collect(::handelState)
        }
    }

    override fun stop() {
        job?.cancel()
        job = null
    }

    private suspend fun handelState(gameState: GameState) {
        if (gameState.currentPlayer != player) {
            return
        }

        delay(1000)

        if (gameState.turnStatus is TurnStatus.Dice) {
            gameEventManager.addEvent(
                GameEvent.DiceRolled(gameState.dice.rolled())
            )
        } else {
            gameEventManager.addEvent(
                GameEvent.PieceMoved(
                    moveCalculator.possibleMoves(gameState).random()
                )
            )
        }
    }
}