package com.toybrokers.ludo.application

import com.toybrokers.ludo.domain.entities.GameState
import com.toybrokers.ludo.domain.entities.Player
import com.toybrokers.ludo.domain.events.GameEvent
import com.toybrokers.ludo.domain.interfaces.GameEventManager
import com.toybrokers.ludo.domain.interfaces.TurnGatekeeper
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