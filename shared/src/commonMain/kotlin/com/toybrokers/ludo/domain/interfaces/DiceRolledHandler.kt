package com.toybrokers.ludo.domain.interfaces

import com.toybrokers.ludo.domain.entities.GameState
import com.toybrokers.ludo.domain.events.GameEvent

interface DiceRolledHandler {
    fun handle(event: GameEvent.DiceRolled, gameState: GameState): GameState
}