package com.toybrokers.ludo.domain.interfaces

import com.toybrokers.ludo.domain.entities.GameState
import com.toybrokers.ludo.domain.events.GameEvent

interface PieceMovedHandler {
    fun handle(event: GameEvent.PieceMoved, gameState: GameState): GameState
}