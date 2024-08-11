package com.toybrokers.ludo.core.domain.interfaces

import com.toybrokers.ludo.core.domain.entities.GameEvent
import com.toybrokers.ludo.core.domain.entities.GameState
import kotlinx.coroutines.flow.StateFlow

interface TurnGatekeeper {
    fun addEvent(event: GameEvent)
    fun undo()
    fun currentState(): StateFlow<GameState>
}