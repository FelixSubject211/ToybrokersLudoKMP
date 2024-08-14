package com.toybrokers.ludo.handlers

import com.toybrokers.ludo.core.domain.entities.Dice
import com.toybrokers.ludo.core.domain.entities.GameState
import com.toybrokers.ludo.core.domain.entities.Player
import com.toybrokers.ludo.core.domain.entities.PlayerPiece
import com.toybrokers.ludo.core.domain.entities.Position
import com.toybrokers.ludo.core.domain.entities.TurnStatus
import com.toybrokers.ludo.core.domain.handlers.DefaultMoveCalculator
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultMoveCalculatorTest {

    private val sut = DefaultMoveCalculator()

    @Test
    fun shouldReturnAllPiecesWhenDiceRollIsSixAndPlayerCanMoveFromHome() {
        val player = Player.Green
        val positions: Map<Position, PlayerPiece> = PlayerPiece.all(player)
            .associateBy { piece -> piece.home() }

        val gameState = GameState(
            positions = positions,
            currentPlayer = player,
            players = setOf(player),
            turnStatus = TurnStatus.Move,
            dice = Dice(diceNumber = 6),
            error = null
        )

        val actual = sut.possibleMoves(gameState)
        val expected = PlayerPiece.all(player).toSet()

        assertEquals(expected, actual)
    }

    @Test
    fun shouldReturnNoPiecesWhenDiceRollIsSixButPlayerHasNotMoved() {
        val player = Player.Green
        val positions: Map<Position, PlayerPiece> = PlayerPiece.all(player)
            .associateBy { piece -> piece.home() }

        val gameState = GameState(
            positions = positions,
            currentPlayer = player,
            players = setOf(player),
            turnStatus = TurnStatus.Dice(remainingAttempts = 2),
            dice = Dice(diceNumber = 6),
            error = null
        )

        val actual = sut.possibleMoves(gameState)
        val expected = setOf<PlayerPiece>()

        assertEquals(expected, actual)
    }

    @Test
    fun shouldReturnOnlyMovablePieceWhenOnlyOnePieceBlockStart() {
        val player = Player.Green
        val positions: Map<Position, PlayerPiece> = PlayerPiece.all(player)
            .associateBy { piece ->
                when(piece) {
                    is PlayerPiece.First -> piece.home()
                    is PlayerPiece.Fourth -> piece.home()
                    is PlayerPiece.Second -> piece.home()
                    is PlayerPiece.Third -> player.start()
                }
            }

        val gameState = GameState(
            positions = positions,
            currentPlayer = player,
            players = setOf(player),
            turnStatus = TurnStatus.Move,
            dice = Dice(diceNumber = 6),
            error = null
        )

        val actual = sut.possibleMoves(gameState)
        val expected = setOf<PlayerPiece>(PlayerPiece.Third(player))

        assertEquals(expected, actual)
    }

    @Test
    fun shouldReturnNoPiecesWhenDiceRollIsLessThanSixAndAllPiecesAreAtHome() {
        val player = Player.Green
        val positions: Map<Position, PlayerPiece> = PlayerPiece.all(player)
            .associateBy { piece -> piece.home() }

        val gameState = GameState(
            positions = positions,
            currentPlayer = player,
            players = setOf(player),
            turnStatus = TurnStatus.Move,
            dice = Dice(diceNumber = 3),
            error = null
        )

        val actual = sut.possibleMoves(gameState)
        val expected = setOf<PlayerPiece>()

        assertEquals(expected, actual)
    }
}