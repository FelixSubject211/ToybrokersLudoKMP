package com.toybrokers.ludo.core.domain.interfaces

import com.toybrokers.ludo.core.domain.entities.GameState
import com.toybrokers.ludo.core.domain.events.GameEvent

interface DiceRolledHandler {
    fun handle(event: GameEvent.DiceRolled, gameState: GameState): GameState
}