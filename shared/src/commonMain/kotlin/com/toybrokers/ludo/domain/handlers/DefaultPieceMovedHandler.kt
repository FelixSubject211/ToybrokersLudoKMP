package com.toybrokers.ludo.domain.handlers

import com.toybrokers.ludo.BuildKonfig
import com.toybrokers.ludo.domain.entities.GameError
import com.toybrokers.ludo.domain.entities.GameState
import com.toybrokers.ludo.domain.entities.Position
import com.toybrokers.ludo.domain.entities.TurnStatus
import com.toybrokers.ludo.domain.events.GameEvent
import com.toybrokers.ludo.domain.interfaces.MoveCalculator
import com.toybrokers.ludo.domain.interfaces.NextPlayerCalculator
import com.toybrokers.ludo.domain.interfaces.PieceMovedHandler

class DefaultPieceMovedHandler(
    private val moveCalculator: MoveCalculator,
    private val nextPlayerCalculator: NextPlayerCalculator
) : PieceMovedHandler {
    override fun handle(event: GameEvent.PieceMoved, gameState: GameState): GameState {
        if (gameState.turnStatus is TurnStatus.Dice) {
            return gameState.copy(error = GameError.MustRollDice)
        }

        if (event.piece.owner != gameState.currentPlayer) {
            return gameState.copy(error = GameError.NotPlayersTurn)
        }

        if (!moveCalculator.possibleMoves(gameState).contains(event.piece)) {
            return gameState.copy(error = GameError.InvalidMove)
        }

        val oldPosition = gameState.positions.filterValues { it == event.piece }.keys.first()

        val newPosition = when (oldPosition) {
            is Position.Home -> event.piece.owner.start()
            is Position.Track -> oldPosition.increment(gameState.dice.diceNumber, event.piece.owner)
            is Position.End -> oldPosition.increment(gameState.dice.diceNumber)
        }

        val opponentPiece = gameState.positions[newPosition]
        val newPositions = gameState.positions
            .toMutableMap()
            .apply {
                opponentPiece?.let {
                    put(Position.Home(it), it)
                    remove(newPosition)
                }
                newPosition?.let {
                    put(it, event.piece)
                    remove(oldPosition)
                }
            }
            .toMap()

        return when(val nextPlayerResult = nextPlayerCalculator.nextPlayer(gameState)) {
            NextPlayerCalculator.Result.GameIsFinish -> {
                gameState
            }
            is NextPlayerCalculator.Result.NextPlayer -> {
                gameState.copy(
                    positions = newPositions,
                    currentPlayer = if (gameState.dice.diceNumberIsMax) {
                        gameState.currentPlayer
                    } else {
                        nextPlayerResult.player
                    },
                    turnStatus = TurnStatus.Dice(remainingAttempts = BuildKonfig.remainingAttempts)
                )
            }
        }
    }
}