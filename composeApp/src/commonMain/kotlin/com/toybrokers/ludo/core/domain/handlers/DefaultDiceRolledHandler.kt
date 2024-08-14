package com.toybrokers.ludo.core.domain.handlers

import com.toybrokers.ludo.BuildKonfig
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

        return handleTurnStatus(updatedState, gameState)
    }

    private fun handleTurnStatus(updatedState: GameState, originalState: GameState): GameState {
        val noPossibleMoves = moveCalculator.possibleMoves(updatedState).isEmpty()

        return when {
            noPossibleMoves && hasRemainingAttempts(originalState) -> retryTurn(updatedState, originalState)
            noPossibleMoves -> handleNoMoves(updatedState, originalState)
            else -> updatedState.copy(turnStatus = TurnStatus.Move, error = null)
        }
    }

    private fun hasRemainingAttempts(gameState: GameState): Boolean {
        val remainingAttempts = gameState.turnStatus.remainingAttemptsOrNull() ?: 0
        return remainingAttempts > 0
    }

    private fun retryTurn(updatedState: GameState, originalState: GameState): GameState {
        val remainingAttempts = (originalState.turnStatus.remainingAttemptsOrNull() ?: 1) - 1
        return updatedState.copy(
            turnStatus = TurnStatus.Dice(remainingAttempts = remainingAttempts),
            error = null
        )
    }

    private fun handleNoMoves(updatedState: GameState, originalState: GameState): GameState {
        return when (val nextPlayerResult = nextPlayerCalculator.nextPlayer(originalState)) {
            NextPlayerCalculator.Result.GameIsFinish -> originalState
            is NextPlayerCalculator.Result.NextPlayer -> updatedState.copy(
                currentPlayer = nextPlayerResult.player,
                turnStatus = TurnStatus.Dice(remainingAttempts = BuildKonfig.remainingAttempts),
                error = null
            )
        }
    }
}