package com.toybrokers.ludo.core.application

import com.toybrokers.ludo.core.domain.entities.GameEvent
import com.toybrokers.ludo.core.domain.entities.GameState
import com.toybrokers.ludo.core.domain.entities.Player
import com.toybrokers.ludo.core.domain.interfaces.GameEventManager
import com.toybrokers.ludo.core.domain.interfaces.TurnGatekeeper
import kotlinx.coroutines.flow.StateFlow

class TurnDefaultGatekeeper(
    private val players: Set<Player>,
    private val canUndo: Boolean = false,
    private val gameEventManager: GameEventManager
): TurnGatekeeper {
    override fun addEvent(event: GameEvent) {
        if (players.contains(gameEventManager.currentState().value.currentPlayer)) {
            gameEventManager.addEvent(event)
        }
    }

    override fun undo() {
        if(canUndo) {
            gameEventManager.undo()
        }
    }

    override fun currentState(): StateFlow<GameState> {
        return gameEventManager.currentState()
    }
}