package com.toybrokers.ludo.domain.handlers

import com.toybrokers.ludo.domain.entities.GameState
import com.toybrokers.ludo.domain.entities.PlayerPiece
import com.toybrokers.ludo.domain.entities.Position
import com.toybrokers.ludo.domain.entities.TurnStatus
import com.toybrokers.ludo.domain.interfaces.MoveCalculator

class DefaultMoveCalculator : MoveCalculator {
    override fun possibleMoves(gameState: GameState): Set<PlayerPiece> {
        return when(gameState.turnStatus) {
            is TurnStatus.Dice -> setOf()
            TurnStatus.Move -> gameState.positions
                .filter { it.value.owner == gameState.currentPlayer }
                .filter { isValidTarget(
                    gameState = gameState,
                    position = it.key,
                    playerPiece = it.value)
                }
                .values.toSet()
        }
    }

    private fun isValidTarget(
        gameState: GameState,
        position: Position,
        playerPiece: PlayerPiece
    ): Boolean {
        return when(position) {
            is Position.Home -> {
                val playerOnStartOrNull = gameState.positions[playerPiece.owner.start()]?.owner
                gameState.dice.diceNumberIsMax && playerOnStartOrNull != gameState.currentPlayer
            }
            is Position.Track -> {
                val incrementedPosition = position.increment(
                    gameState.dice.diceNumber,
                    playerPiece.owner
                )

                return if (incrementedPosition == null) {
                    false
                } else {
                    gameState.positions[incrementedPosition]?.owner != gameState.currentPlayer
                }
            }
            is Position.End -> {
                val incrementedPosition = position.increment(gameState.dice.diceNumber)
                return if (incrementedPosition == null) {
                    false
                } else {
                    gameState.positions[incrementedPosition] == null
                }
            }
        }
    }
}
