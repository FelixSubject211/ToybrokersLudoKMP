package com.toybrokers.ludo.controllers

import com.toybrokers.ludo.entities.GameEvent
import com.toybrokers.ludo.entities.GameState

interface GameEventManager {
    fun addEvent(event: GameEvent): GameState
    fun getCurrentState(): GameState
}