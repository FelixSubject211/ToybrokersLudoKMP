package com.toybrokers.ludo.core.domain.interfaces

import com.toybrokers.ludo.core.domain.entities.GameState
import com.toybrokers.ludo.core.domain.entities.PlayerPiece

interface MoveCalculator {
    fun possibleMoves(gameState: GameState): Set<PlayerPiece>
}