package com.toybrokers.ludo.core.domain.interfaces

import com.toybrokers.ludo.core.domain.entities.GameState
import com.toybrokers.ludo.core.domain.entities.Player

interface NextPlayerCalculator {
    fun nextPlayer(gameState: GameState): Result

    sealed class Result {
        data class NextPlayer(val player: Player): Result()
        data object GameIsFinish: Result()
    }
}