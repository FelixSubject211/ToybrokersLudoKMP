package com.toybrokers.ludo.domain.interfaces

import com.toybrokers.ludo.domain.entities.GameEvent
import com.toybrokers.ludo.domain.entities.GameState

interface GameEventManager {
    fun addEvent(event: GameEvent): GameState
    fun undo(): GameState
    fun getCurrentState(): GameState
}