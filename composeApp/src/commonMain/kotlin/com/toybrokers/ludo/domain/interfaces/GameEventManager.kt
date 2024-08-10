package com.toybrokers.ludo.domain.interfaces

import com.toybrokers.ludo.domain.entities.GameEvent
import com.toybrokers.ludo.domain.entities.GameState
import kotlinx.coroutines.flow.StateFlow

interface GameEventManager {
    fun addEvent(event: GameEvent)
    fun undo()
    fun currentState(): StateFlow<GameState>
}