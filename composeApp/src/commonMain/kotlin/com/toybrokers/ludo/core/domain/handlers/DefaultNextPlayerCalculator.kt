package com.toybrokers.ludo.core.domain.handlers

import com.toybrokers.ludo.core.domain.entities.GameState
import com.toybrokers.ludo.core.domain.entities.Player
import com.toybrokers.ludo.core.domain.entities.Player.Blue
import com.toybrokers.ludo.core.domain.entities.Player.Green
import com.toybrokers.ludo.core.domain.entities.Player.Red
import com.toybrokers.ludo.core.domain.entities.Player.Yellow
import com.toybrokers.ludo.core.domain.entities.Position
import com.toybrokers.ludo.core.domain.interfaces.NextPlayerCalculator

class DefaultNextPlayerCalculator : NextPlayerCalculator {
    override fun nextPlayer(gameState: GameState): NextPlayerCalculator.Result {
        val allPlayersInEnd = gameState.players.all { playerHasAllPiecesInEnd(it, gameState) }

        if (allPlayersInEnd) {
            return NextPlayerCalculator.Result.GameIsFinish
        }

        val nextPlayer = generateSequence(gameState.currentPlayer) {
            nextPlayer(it)
        }
            .drop(1)
            .first { player ->
                player in gameState.players && !playerHasAllPiecesInEnd(player, gameState)
            }

        return NextPlayerCalculator.Result.NextPlayer(nextPlayer)
    }

    private fun nextPlayer(player: Player): Player {
        return when (player) {
            is Blue -> Green
            is Green -> Yellow
            is Yellow -> Red
            is Red -> Blue
        }
    }

    private fun playerHasAllPiecesInEnd(player: Player, gameState: GameState): Boolean {
        return gameState.positions
            .filterValues { it.owner == player }
            .all { it.key is Position.End }
    }
}