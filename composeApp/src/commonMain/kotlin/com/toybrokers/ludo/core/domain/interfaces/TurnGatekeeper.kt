package com.toybrokers.ludo.core.domain.interfaces

import com.toybrokers.ludo.core.domain.entities.GameState
import com.toybrokers.ludo.core.domain.events.GameEvent
import kotlinx.coroutines.flow.StateFlow

interface TurnGatekeeper {
    fun addEvent(event: GameEvent)
    fun undo()
    fun currentState(): StateFlow<GameState>
}