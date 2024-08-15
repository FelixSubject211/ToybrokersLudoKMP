package com.toybrokers.ludo.application

import com.toybrokers.ludo.domain.entities.GameState
import com.toybrokers.ludo.domain.entities.Player
import com.toybrokers.ludo.domain.entities.TurnStatus
import com.toybrokers.ludo.domain.events.GameEvent
import com.toybrokers.ludo.domain.interfaces.GameEventManager
import com.toybrokers.ludo.domain.interfaces.MoveCalculator
import com.toybrokers.ludo.domain.interfaces.Opponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class DefaultOpponent(
    private val player: Player,
    private val gameEventManager: GameEventManager,
    private val moveCalculator: MoveCalculator
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