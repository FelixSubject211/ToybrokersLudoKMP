package com.toybrokers.ludo.core.domain.handlers

import com.toybrokers.ludo.core.domain.entities.GameError
import com.toybrokers.ludo.core.domain.entities.GameState
import com.toybrokers.ludo.core.domain.entities.TurnStatus
import com.toybrokers.ludo.core.domain.events.GameEvent
import com.toybrokers.ludo.core.domain.interfaces.DiceRolledHandler
import com.toybrokers.ludo.core.domain.interfaces.MoveCalculator
import com.toybrokers.ludo.core.domain.interfaces.NextPlayerCalculator
import com.toybrokers.ludo.di.Koin
import org.koin.core.component.get

class DefaultDiceRolledHandler(
    private val moveCalculator: MoveCalculator = Koin.get(),
    private val nextPlayerCalculator: NextPlayerCalculator = Koin.get()
) : DiceRolledHandler {
    override fun handle(event: GameEvent.DiceRolled, gameState: GameState): GameState {
        if (gameState.turnStatus == TurnStatus.Move) {
            return gameState.copy(error = GameError.MustMovePiece)
        }

        val updatedState = gameState.copy(
            dice = event.dice,
            turnStatus = TurnStatus.Move
        )
        val hasNoPossibleMoves = moveCalculator.possibleMoves(updatedState).isEmpty()

        return when {
            hasNoPossibleMoves && (gameState.turnStatus.remainingAttemptsOrNull() ?: 0) > 0 -> {
                updatedState.copy(
                    turnStatus = TurnStatus.Dice(
                        remainingAttempts = (gameState.turnStatus.remainingAttemptsOrNull() ?: 1) - 1
                    ),
                    error = null
                )
            }
            hasNoPossibleMoves -> {
                updatedState.copy(
                    currentPlayer = nextPlayerCalculator.nextPlayer(
                        currentPlayer = gameState.currentPlayer,
                        players = gameState.players
                    ),
                    turnStatus = TurnStatus.Dice(remainingAttempts = 2),
                    error = null
                )
            }
            else -> {
                updatedState.copy(turnStatus = TurnStatus.Move, error = null)
            }
        }
    }
}