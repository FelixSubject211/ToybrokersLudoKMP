package com.toybrokers.ludo.core.application

import com.toybrokers.ludo.core.domain.entities.GameEvent
import com.toybrokers.ludo.core.domain.entities.GameState
import com.toybrokers.ludo.core.domain.entities.Player
import com.toybrokers.ludo.core.domain.entities.TurnStatus
import com.toybrokers.ludo.core.domain.interfaces.GameEventManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Opponent(
    private val player: Player,
    private val gameEventManager: GameEventManager
) {
    private var job: Job? = null

    fun start() {
        job = CoroutineScope(Dispatchers.Default).launch {
            gameEventManager.currentState()
                .collect(::handelState)
        }
    }

    fun stop() {
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
                GameEvent.DiceRolled((1..6).random())
            )
        } else {
            gameEventManager.addEvent(
                GameEvent.PieceMoved(
                    gameState.possibleMoves().random()
                )
            )
        }
    }
}