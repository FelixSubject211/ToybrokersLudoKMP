package com.toybrokers.ludo.core.domain.handlers

import com.toybrokers.ludo.core.domain.entities.GameError
import com.toybrokers.ludo.core.domain.entities.GameState
import com.toybrokers.ludo.core.domain.entities.Position
import com.toybrokers.ludo.core.domain.entities.TurnStatus
import com.toybrokers.ludo.core.domain.events.GameEvent
import com.toybrokers.ludo.core.domain.interfaces.MoveCalculator
import com.toybrokers.ludo.core.domain.interfaces.NextPlayerCalculator
import com.toybrokers.ludo.core.domain.interfaces.PieceMovedHandler
import com.toybrokers.ludo.di.Koin
import org.koin.core.component.get

class DefaultPieceMovedHandler(
    private val moveCalculator: MoveCalculator = Koin.get(),
    private val nextPlayerCalculator: NextPlayerCalculator = Koin.get()
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

        return gameState.copy(
            positions = newPositions,
            currentPlayer = if (gameState.dice.diceNumberIsMax) {
                gameState.currentPlayer
            } else {
                nextPlayerCalculator.nextPlayer(gameState.currentPlayer, gameState.players)
            },
            turnStatus = TurnStatus.Dice(remainingAttempts = 2)
        )
    }
}