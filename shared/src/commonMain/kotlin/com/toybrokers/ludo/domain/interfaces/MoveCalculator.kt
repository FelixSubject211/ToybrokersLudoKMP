package com.toybrokers.ludo.domain.interfaces

import com.toybrokers.ludo.domain.entities.GameState
import com.toybrokers.ludo.domain.entities.PlayerPiece

interface MoveCalculator {
    fun possibleMoves(gameState: GameState): Set<PlayerPiece>
}