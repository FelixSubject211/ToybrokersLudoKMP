package ludo.handlers

import com.toybrokers.ludo.BuildKonfig
import com.toybrokers.ludo.domain.entities.Dice
import com.toybrokers.ludo.domain.entities.GameError
import com.toybrokers.ludo.domain.entities.GameState
import com.toybrokers.ludo.domain.entities.Player
import com.toybrokers.ludo.domain.entities.PlayerPiece
import com.toybrokers.ludo.domain.entities.TurnStatus
import com.toybrokers.ludo.domain.events.GameEvent
import com.toybrokers.ludo.domain.handlers.DefaultDiceRolledHandler
import com.toybrokers.ludo.domain.interfaces.MoveCalculator
import com.toybrokers.ludo.domain.interfaces.NextPlayerCalculator
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import ludo.testUtils.positions
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultDiceRolledHandlerTest {
    private lateinit var sut: DefaultDiceRolledHandler

    private var mockMoveCalculator = mock<MoveCalculator>()
    private var mockNextPlayerCalculator = mock<NextPlayerCalculator>()

    @BeforeTest
    fun setup() {
        sut = DefaultDiceRolledHandler(
            moveCalculator = mockMoveCalculator,
            nextPlayerCalculator = mockNextPlayerCalculator
        )
    }

    @Test
    fun shouldReturnMustMovePieceErrorWhenTurnStatusIsMove() {
        val gameState = GameState(
            positions = positions(playersInHomeAndTrackAndEnd = setOf(Player.Green, Player.Blue)),
            currentPlayer = Player.Green,
            players = setOf(Player.Green, Player.Blue),
            turnStatus = TurnStatus.Move,
            dice = Dice(diceNumber = 1),
            error = null
        )

        val event = GameEvent.DiceRolled(dice = Dice(1))

        val actual = sut.handle(event, gameState)
        val expected = gameState.copy(error = GameError.MustMovePiece)

        assertEquals(expected, actual)
    }

    @Test
    fun shouldReturnTurnStatusDiceWhenMoveCalculatorReturnNoPossibleMovesAndRemainingAttempts() {
        val gameState = GameState(
            positions = positions(playersInHomeAndTrackAndEnd = setOf(Player.Green, Player.Blue)),
            currentPlayer = Player.Green,
            players = setOf(Player.Green, Player.Blue),
            turnStatus = TurnStatus.Dice(remainingAttempts = 2),
            dice = Dice(diceNumber = 1),
            error = null
        )

        val event = GameEvent.DiceRolled(dice = Dice(2))

        every {
            mockMoveCalculator.possibleMoves(gameState = any())
        } returns emptySet()

        val actual = sut.handle(event, gameState)
        val expected = gameState.copy(
            turnStatus = TurnStatus.Dice(remainingAttempts = 1),
            dice = Dice(diceNumber = 2)
        )

        assertEquals(expected, actual)
    }

    @Test
    fun shouldReturnTurnStatusDiceAndNextPlayerWhenMoveCalculatorReturnNoPossibleMovesAndNextPlayerCalculatorReturnPlayer() {
        val gameState = GameState(
            positions = positions(playersInHomeAndTrackAndEnd = setOf(Player.Green, Player.Blue)),
            currentPlayer = Player.Green,
            players = setOf(Player.Green, Player.Blue),
            turnStatus = TurnStatus.Dice(remainingAttempts = 0),
            dice = Dice(diceNumber = 1),
            error = null
        )

        val event = GameEvent.DiceRolled(dice = Dice(2))

        every {
            mockMoveCalculator.possibleMoves(gameState = any())
        } returns emptySet()

        every {
            mockNextPlayerCalculator.nextPlayer(gameState = any())
        } returns NextPlayerCalculator.Result.NextPlayer(Player.Blue)

        val actual = sut.handle(event, gameState)
        val expected = gameState.copy(
            currentPlayer = Player.Blue,
            turnStatus = TurnStatus.Dice(remainingAttempts = BuildKonfig.remainingAttempts),
            dice = Dice(diceNumber = 2)
        )

        assertEquals(expected, actual)
    }

    @Test
    fun shouldReturnInputWhenMoveCalculatorReturnNoPossibleMovesAndNextPlayerCalculatorReturnGameFinish() {
        val gameState = GameState(
            positions = positions(playersInEnd = setOf(Player.Green, Player.Blue)),
            currentPlayer = Player.Green,
            players = setOf(Player.Green, Player.Blue),
            turnStatus = TurnStatus.Dice(remainingAttempts = 0),
            dice = Dice(diceNumber = 1),
            error = null
        )

        val event = GameEvent.DiceRolled(dice = Dice(2))

        every {
            mockMoveCalculator.possibleMoves(gameState = any())
        } returns emptySet()

        every {
            mockNextPlayerCalculator.nextPlayer(gameState = any())
        } returns NextPlayerCalculator.Result.GameIsFinish

        val actual = sut.handle(event, gameState)

        assertEquals(gameState, actual)
    }

    @Test
    fun shouldReturnTurnStatusMoveWhenMoveCalculatorReturnPossibleMoves() {
        val gameState = GameState(
            positions = positions(playersInHomeAndTrackAndEnd = setOf(Player.Green, Player.Blue)),
            currentPlayer = Player.Green,
            players = setOf(Player.Green, Player.Blue),
            turnStatus = TurnStatus.Dice(remainingAttempts = 0),
            dice = Dice(diceNumber = 1),
            error = null
        )

        val event = GameEvent.DiceRolled(dice = Dice(2))

        every {
            mockMoveCalculator.possibleMoves(gameState = any())
        } returns setOf(PlayerPiece.First(Player.Green))

        val actual = sut.handle(event, gameState)
        val expected = gameState.copy(
            currentPlayer = Player.Green,
            turnStatus = TurnStatus.Move,
            dice = Dice(diceNumber = 2)
        )

        assertEquals(expected, actual)
    }
}