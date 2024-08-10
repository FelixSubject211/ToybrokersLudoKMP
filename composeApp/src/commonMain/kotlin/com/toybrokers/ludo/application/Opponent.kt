package com.toybrokers.ludo.application

import androidx.lifecycle.Lifecycle
import com.toybrokers.ludo.domain.entities.GameEvent
import com.toybrokers.ludo.domain.entities.GameState
import com.toybrokers.ludo.domain.entities.Player
import com.toybrokers.ludo.domain.entities.PlayerPiece
import com.toybrokers.ludo.domain.entities.TurnStatus
import com.toybrokers.ludo.domain.interfaces.GameEventManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Opponent(
    private val player: Player,
    private val gameEventManager: GameEventManager
) {
    fun start() {
        CoroutineScope(Dispatchers.Default).launch {
            gameEventManager.currentState()
                .collect(::handelState)
        }
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