package com.toybrokers.ludo.core.domain.entities

import com.toybrokers.ludo.core.domain.events.GameEventError

data class GameState(
    val positions: Map<Position, PlayerPiece>,
    val currentPlayer: Player,
    val players: Set<Player>,
    val turnStatus: TurnStatus,
    val diceNumber: Int,
    val error: GameEventError?
) {
    fun apply(event: GameEvent): GameState {
        return when (event) {
            is GameEvent.PieceMoved -> pieceMoved(event)
            is GameEvent.DiceRolled -> diceRolled(event)
        }
    }

    private fun pieceMoved(event: GameEvent.PieceMoved): GameState {
        if (turnStatus is TurnStatus.Dice) {
            return copy(error = GameEventError.MustRollDice)
        }

        if (event.piece.owner != currentPlayer) {
            return copy(error = GameEventError.NotPlayersTurn)
        }

        if (!possibleMoves().contains(event.piece)) {
            return copy(error = GameEventError.InvalidMove)
        }

        val oldPosition = positions.filterValues { it == event.piece }.keys.first()

        val newPosition = when (oldPosition) {
            is Position.Home -> event.piece.owner.start()
            is Position.Track -> oldPosition.increment(diceNumber, event.piece.owner)
            is Position.End -> oldPosition.increment(diceNumber)
        }

        val opponentPiece = positions[newPosition]
        val newPositions = positions
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

        return copy(
            positions = newPositions,
            currentPlayer = if (diceNumber == 6) currentPlayer else nextPlayer(),
            turnStatus = TurnStatus.Dice(remainingAttempts = 2)
        )
    }

    private fun diceRolled(event: GameEvent.DiceRolled): GameState {
        if (turnStatus == TurnStatus.Move) {
            return this.copy(error = GameEventError.MustMovePiece)
        }

        val updatedState = this.copy(diceNumber = event.diceNumber)
        val hasNoPossibleMoves = updatedState.possibleMoves().isEmpty()

        return when {
            hasNoPossibleMoves && (turnStatus.remainingAttemptsOrNull() ?: 0) > 0 -> {
                updatedState.copy(
                    turnStatus = TurnStatus.Dice(
                        remainingAttempts = (turnStatus.remainingAttemptsOrNull() ?: 1) - 1
                    ),
                    error = null
                )
            }
            hasNoPossibleMoves -> {
                updatedState.copy(
                    currentPlayer = nextPlayer(),
                    turnStatus = TurnStatus.Dice(remainingAttempts = 2),
                    error = null
                )
            }
            else -> {
                updatedState.copy(turnStatus = TurnStatus.Move, error = null)
            }
        }
    }

    private fun nextPlayer(): Player {
        return generateSequence(currentPlayer) { it.next() }
            .drop(1)
            .first { it in players }
    }

    fun possibleMoves(): List<PlayerPiece> {
        return positions
            .filter { it.value.owner == currentPlayer }
            .filter { hasValidTarget(position = it.key, playerPiece = it.value) }
            .values.toList()
    }

    private fun hasValidTarget(position: Position, playerPiece: PlayerPiece): Boolean {
        return when(position) {
            is Position.Home -> {
                diceNumber == 6 && positions[playerPiece.owner.start()]?.owner != currentPlayer
            }
            is Position.Track -> {
                val incrementedPosition = position.increment(diceNumber, playerPiece.owner)
                return if (incrementedPosition == null) {
                    false
                } else {
                    positions[incrementedPosition]?.owner != currentPlayer
                }
            }
            is Position.End -> {
                val incrementedPosition = position.increment(diceNumber)
                return if (incrementedPosition == null) {
                    false
                } else {
                    positions[incrementedPosition] == null
                }
            }
        }
    }

    companion object {
        fun initialState(players: Set<Player>): GameState {
            val positions: Map<Position, PlayerPiece> = players
                .flatMap { player ->
                    PlayerPiece.all(player).map { piece -> piece.home() to piece }
                }
                .toMap()

            return GameState(
                positions = positions,
                currentPlayer = players.random(),
                players = players,
                turnStatus = TurnStatus.Dice(remainingAttempts = 2),
                diceNumber = 1,
                error = null
            )
        }
    }
}