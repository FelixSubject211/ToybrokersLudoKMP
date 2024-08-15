package com.toybrokers.ludo.domain.interfaces

import com.toybrokers.ludo.domain.entities.GameState
import com.toybrokers.ludo.domain.entities.Player

interface NextPlayerCalculator {
    fun nextPlayer(gameState: GameState): Result

    sealed class Result {
        data class NextPlayer(val player: Player): Result()
        data object GameIsFinish: Result()
    }
}