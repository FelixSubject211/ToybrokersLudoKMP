package com.toybrokers.ludo.entities

import kotlin.test.Test
import kotlin.test.assertEquals

class GameStateTest {

    @Test
    fun shouldReturnErrorWhenMovingPieceFromHomeWhileDiceRollIs3() {
        val playerPiece1 = PlayerPiece.First(Player.Green)
        val playerPiece2 = PlayerPiece.Second(Player.Green)

        val gameState = GameState(
            positions = mapOf(
                Position.Home(playerPiece1) to playerPiece1,
                Position.Track(3) to playerPiece2
            ),
            currentPlayer = Player.Green,
            players = listOf(Player.Green),
            turnStatus = TurnStatus.Move,
            diceNumber = 3,
            error = null
        )

        val actual = gameState.apply(GameEvent.PieceMoved(playerPiece1))

        val expected = gameState.copy(
            error = GameEventError.InvalidMove
        )

        assertEquals(expected, actual)
    }

    @Test
    fun shouldCaptureOpponentPieceWhenMovingOntoOccupiedPosition() {
        val playerPiece = PlayerPiece.First(Player.Green)
        val opponentPiece = PlayerPiece.First(Player.Red)

        val gameState = GameState(
            positions = mapOf(
                Position.Track(0) to playerPiece,
                Position.Track(3) to opponentPiece
            ),
            currentPlayer = Player.Green,
            players = listOf(Player.Green, Player.Red),
            turnStatus = TurnStatus.Move,
            diceNumber = 3,
            error = null
        )

        val actual = gameState.apply(GameEvent.PieceMoved(playerPiece))

        val expected = gameState.copy(
            positions = mapOf(
                Position.Track(3) to playerPiece,
                Position.Home(opponentPiece) to opponentPiece,
            ),
            currentPlayer = Player.Red,
            diceNumber = 3,
            turnStatus = TurnStatus.Dice(remainingAttempts = 2)
        )

        assertEquals(expected, actual)
    }
}