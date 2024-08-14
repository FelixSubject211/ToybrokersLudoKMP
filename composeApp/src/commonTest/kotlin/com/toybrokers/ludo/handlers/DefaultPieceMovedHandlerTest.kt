package com.toybrokers.ludo.handlers

import com.toybrokers.ludo.BuildKonfig
import com.toybrokers.ludo.core.domain.entities.Dice
import com.toybrokers.ludo.core.domain.entities.GameError
import com.toybrokers.ludo.core.domain.entities.GameState
import com.toybrokers.ludo.core.domain.entities.Player
import com.toybrokers.ludo.core.domain.entities.PlayerPiece
import com.toybrokers.ludo.core.domain.entities.Position
import com.toybrokers.ludo.core.domain.entities.TurnStatus
import com.toybrokers.ludo.core.domain.events.GameEvent
import com.toybrokers.ludo.core.domain.handlers.DefaultPieceMovedHandler
import com.toybrokers.ludo.core.domain.interfaces.MoveCalculator
import com.toybrokers.ludo.core.domain.interfaces.NextPlayerCalculator
import com.toybrokers.ludo.testUtils.positions
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultPieceMovedHandlerTest {
    private lateinit var sut: DefaultPieceMovedHandler

    private var mockMoveCalculator = mock<MoveCalculator>()
    private var mockNextPlayerCalculator = mock<NextPlayerCalculator>()

    @BeforeTest
    fun setup() {
        sut = DefaultPieceMovedHandler(
            moveCalculator = mockMoveCalculator,
            nextPlayerCalculator = mockNextPlayerCalculator
        )
    }

    @Test
    fun shouldReturnMustRollDiceErrorWhenTurnStatusIsDice() {
        val gameState = GameState(
            positions = positions(playersInHomeAndTrackAndEnd = setOf(Player.Green, Player.Blue)),
            currentPlayer = Player.Green,
            players = setOf(Player.Green, Player.Blue),
            turnStatus = TurnStatus.Dice(remainingAttempts = 2),
            dice = Dice(diceNumber = 1),
            error = null
        )

        val event = GameEvent.PieceMoved(piece = PlayerPiece.First(Player.Green))

        val actual = sut.handle(event, gameState)
        val expected = gameState.copy(error = GameError.MustRollDice)

        assertEquals(expected, actual)
    }

    @Test
    fun shouldReturnNotPlayersTurnErrorWhenPlayerPieceOwnerIsNotCurrentPlayer() {
        val gameState = GameState(
            positions = positions(playersInHomeAndTrackAndEnd = setOf(Player.Green, Player.Blue)),
            currentPlayer = Player.Green,
            players = setOf(Player.Green, Player.Blue),
            turnStatus = TurnStatus.Move,
            dice = Dice(diceNumber = 1),
            error = null
        )

        val event = GameEvent.PieceMoved(piece = PlayerPiece.First(Player.Blue))

        val actual = sut.handle(event, gameState)
        val expected = gameState.copy(error = GameError.NotPlayersTurn)

        assertEquals(expected, actual)
    }

    @Test
    fun shouldReturnInvalidMoveErrorWhenPossibleMovesNotContainsPlayerPiece() {
        val gameState = GameState(
            positions = positions(playersInHomeAndTrackAndEnd = setOf(Player.Green, Player.Blue)),
            currentPlayer = Player.Green,
            players = setOf(Player.Green, Player.Blue),
            turnStatus = TurnStatus.Move,
            dice = Dice(diceNumber = 1),
            error = null
        )

        every {
            mockMoveCalculator.possibleMoves(gameState = any())
        } returns emptySet()

        val event = GameEvent.PieceMoved(piece = PlayerPiece.First(Player.Green))

        val actual = sut.handle(event, gameState)
        val expected = gameState.copy(error = GameError.InvalidMove)

        assertEquals(expected, actual)
    }

    @Test
    fun shouldReturnInputWhenNextPlayerCalculatorReturnGameFinish() {
        val gameState = GameState(
            positions = positions(playersInHomeAndTrackAndEnd = setOf(Player.Green, Player.Blue)),
            currentPlayer = Player.Green,
            players = setOf(Player.Green, Player.Blue),
            turnStatus = TurnStatus.Move,
            dice = Dice(diceNumber = 1),
            error = null
        )

        every {
            mockMoveCalculator.possibleMoves(gameState = any())
        } returns setOf(PlayerPiece.First(Player.Green))

        every {
            mockNextPlayerCalculator.nextPlayer(gameState = any())
        } returns NextPlayerCalculator.Result.GameIsFinish

        val event = GameEvent.PieceMoved(piece = PlayerPiece.First(Player.Green))

        val actual = sut.handle(event, gameState)

        assertEquals(gameState, actual)
    }

    @Test
    fun shouldReturnMovedStateWithCurrentPlayerWhenDiceNumberIsMax() {
        val gameState = GameState(
            positions = mapOf(
                Position.Home(PlayerPiece.First(Player.Green)) to PlayerPiece.First(Player.Green),
                Position.Home(PlayerPiece.First(Player.Blue)) to PlayerPiece.First(Player.Blue)
            ),
            currentPlayer = Player.Green,
            players = setOf(Player.Green, Player.Blue),
            turnStatus = TurnStatus.Move,
            dice = Dice(diceNumber = BuildKonfig.maxDiceNumber),
            error = null
        )

        every {
            mockMoveCalculator.possibleMoves(gameState = any())
        } returns setOf(PlayerPiece.First(Player.Green))

        every {
            mockNextPlayerCalculator.nextPlayer(gameState = any())
        } returns NextPlayerCalculator.Result.NextPlayer(Player.Blue)

        val event = GameEvent.PieceMoved(piece = PlayerPiece.First(Player.Green))

        val actual = sut.handle(event, gameState)
        val expected = gameState.copy(
            positions = mapOf(
                Position.Home(PlayerPiece.First(Player.Blue)) to PlayerPiece.First(Player.Blue),
                Player.Green.start() to PlayerPiece.First(Player.Green)
            ),
            turnStatus = TurnStatus.Dice(remainingAttempts = BuildKonfig.remainingAttempts)
        )

        assertEquals(expected, actual)
    }

    @Test
    fun shouldReturnMovedStateWithNextPlayerWhenDiceNumberIsNotMax() {
        val gameState = GameState(
            positions = mapOf(
                Player.Green.start() to PlayerPiece.First(Player.Green),
                Position.Home(PlayerPiece.First(Player.Blue)) to PlayerPiece.First(Player.Blue)
            ),
            currentPlayer = Player.Green,
            players = setOf(Player.Green, Player.Blue),
            turnStatus = TurnStatus.Move,
            dice = Dice(diceNumber = 1),
            error = null
        )

        every {
            mockMoveCalculator.possibleMoves(gameState = any())
        } returns setOf(PlayerPiece.First(Player.Green))

        every {
            mockNextPlayerCalculator.nextPlayer(gameState = any())
        } returns NextPlayerCalculator.Result.NextPlayer(Player.Blue)

        val event = GameEvent.PieceMoved(piece = PlayerPiece.First(Player.Green))

        val actual = sut.handle(event, gameState)
        val expected = gameState.copy(
            positions = mapOf(
                Position.Home(PlayerPiece.First(Player.Blue)) to PlayerPiece.First(Player.Blue),
                (Player.Green.start() as Position.Track).increment(1, Player.Green)!!
                        to PlayerPiece.First(Player.Green)
            ),
            turnStatus = TurnStatus.Dice(remainingAttempts = BuildKonfig.remainingAttempts),
            currentPlayer = Player.Blue
        )

        assertEquals(expected, actual)
    }

    @Test
    fun shouldPullOpponentPlayerOnTargetBackToHome() {
        val gameState = GameState(
            positions = mapOf(
                Position.Track(0) to PlayerPiece.First(Player.Green),
                Position.Track(1) to PlayerPiece.First(Player.Blue)
            ),
            currentPlayer = Player.Green,
            players = setOf(Player.Green, Player.Blue),
            turnStatus = TurnStatus.Move,
            dice = Dice(diceNumber = 1),
            error = null
        )

        every {
            mockMoveCalculator.possibleMoves(gameState = any())
        } returns setOf(PlayerPiece.First(Player.Green))

        every {
            mockNextPlayerCalculator.nextPlayer(gameState = any())
        } returns NextPlayerCalculator.Result.NextPlayer(Player.Blue)

        val event = GameEvent.PieceMoved(piece = PlayerPiece.First(Player.Green))

        val actual = sut.handle(event, gameState)
        val expected = gameState.copy(
            positions = mapOf(
                Position.Home(PlayerPiece.First(Player.Blue)) to PlayerPiece.First(Player.Blue),
                Position.Track(1) to PlayerPiece.First(Player.Green),
            ),
            turnStatus = TurnStatus.Dice(remainingAttempts = BuildKonfig.remainingAttempts),
            currentPlayer = Player.Blue
        )

        assertEquals(expected, actual)
    }
}