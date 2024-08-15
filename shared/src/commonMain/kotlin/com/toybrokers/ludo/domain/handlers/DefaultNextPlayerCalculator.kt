package com.toybrokers.ludo.domain.handlers


import com.toybrokers.ludo.domain.entities.GameState
import com.toybrokers.ludo.domain.entities.Player
import com.toybrokers.ludo.domain.entities.Position
import com.toybrokers.ludo.domain.interfaces.NextPlayerCalculator

class DefaultNextPlayerCalculator : NextPlayerCalculator {
    override fun nextPlayer(gameState: GameState): NextPlayerCalculator.Result {
        val allPlayersInEnd = gameState.players.all { playerHasAllPiecesInEnd(it, gameState) }

        if (allPlayersInEnd) {
            return NextPlayerCalculator.Result.GameIsFinish
        }

        val nextPlayer = generateSequence(gameState.currentPlayer) {
            next(it)
        }
            .drop(1)
            .first { player ->
                player in gameState.players && !playerHasAllPiecesInEnd(player, gameState)
            }

        return NextPlayerCalculator.Result.NextPlayer(nextPlayer)
    }

    private fun next(player: Player): Player {
        return when (player) {
            is Player.Blue -> Player.Green
            is Player.Green -> Player.Yellow
            is Player.Yellow -> Player.Red
            is Player.Red -> Player.Blue
        }
    }

    private fun playerHasAllPiecesInEnd(player: Player, gameState: GameState): Boolean {
        return gameState.positions
            .filterValues { it.owner == player }
            .all { it.key is Position.End }
    }
}