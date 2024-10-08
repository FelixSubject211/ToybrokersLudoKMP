package com.toybrokers.ludo.domain.interfaces

import com.toybrokers.ludo.domain.entities.GameState
import com.toybrokers.ludo.domain.events.GameEvent
import kotlinx.coroutines.flow.StateFlow


interface GameEventManager {
    fun addEvent(event: GameEvent)
    fun undo()
    fun currentState(): StateFlow<GameState>
}