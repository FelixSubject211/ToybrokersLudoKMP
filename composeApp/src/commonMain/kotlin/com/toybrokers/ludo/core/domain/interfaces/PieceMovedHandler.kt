package com.toybrokers.ludo.core.domain.interfaces

import com.toybrokers.ludo.core.domain.entities.GameState
import com.toybrokers.ludo.core.domain.events.GameEvent

interface PieceMovedHandler {
    fun handle(event: GameEvent.PieceMoved, gameState: GameState): GameState
}