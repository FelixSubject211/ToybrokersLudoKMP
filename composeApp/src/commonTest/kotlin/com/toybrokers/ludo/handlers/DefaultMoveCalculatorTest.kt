package com.toybrokers.ludo.handlers

import com.toybrokers.ludo.BuildKonfig
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
    fun shouldNotAllowMoveWhenTurnStatusIsDice() {
        val testCases = listOf(
            TestCase(
                positions = PlayerPiece.all(Player.Green).associateBy { it.home() },
                turnStatus = TurnStatus.Dice(remainingAttempts = 1),
                currentPlayer = Player.Green,
                diceNumber = BuildKonfig.maxDiceNumber,
                expectedMoves = emptySet()
            ),
        )

        runTestCases(testCases)
    }

    @Test
    fun shouldAllowMoveFromHomeIfDiceNumberIsMaxAndStartFreeFromOwnPlayer() {
        val testCases = listOf(
            TestCase(
                positions = PlayerPiece.all(Player.Green).associateBy { it.home() },
                turnStatus = TurnStatus.Move,
                currentPlayer = Player.Green,
                diceNumber = BuildKonfig.maxDiceNumber,
                expectedMoves = PlayerPiece.all(Player.Green).toSet()
            ),
            TestCase(
                positions = PlayerPiece.all(Player.Green).associateBy { it.home() },
                currentPlayer = Player.Green,
                turnStatus = TurnStatus.Move,
                diceNumber = 1,
                expectedMoves = emptySet()
            ),
            TestCase(
                positions = PlayerPiece.all(Player.Green)
                    .associateBy { piece ->
                        when(piece) {
                            is PlayerPiece.Third -> Player.Green.start()
                            else -> piece.home()
                        }
                    },
                turnStatus = TurnStatus.Move,
                currentPlayer = Player.Green,
                diceNumber = BuildKonfig.maxDiceNumber,
                expectedMoves = setOf(PlayerPiece.Third(Player.Green))
            ),
        )

        runTestCases(testCases)
    }

    @Test
    fun shouldAllowMoveIfTargetNotContainsOwnPlayerPiece() {
        val testCases = listOf(
            TestCase(
                positions = mapOf(
                    Position.Track(0) to PlayerPiece.First(Player.Green),
                    Position.Track(1) to PlayerPiece.Second(Player.Green),
                    Position.Track(2) to PlayerPiece.First(Player.Red),
                    Position.Track(4) to PlayerPiece.Third(Player.Green)
                ),
                currentPlayer = Player.Green,
                turnStatus = TurnStatus.Move,
                diceNumber = 1,
                expectedMoves = setOf(
                    PlayerPiece.Second(Player.Green),
                    PlayerPiece.Third(Player.Green)
                )
            )
        )

        runTestCases(testCases)
    }

    @Test
    fun shouldAllowMoveToEndIfTargetIsEmpty() {
        val testCases = listOf(
            TestCase(
                positions = mapOf(
                    Position.Track(BuildKonfig.greenLastIndexBeforeEnd - 1) to PlayerPiece.First(Player.Green),
                    Position.Track(BuildKonfig.greenLastIndexBeforeEnd) to PlayerPiece.Second(Player.Green),
                    Position.End(Player.Green, 0) to PlayerPiece.Third(Player.Green),
                ),
                currentPlayer = Player.Green,
                diceNumber = 2,
                turnStatus = TurnStatus.Move,
                expectedMoves = setOf(
                    PlayerPiece.Second(Player.Green),
                    PlayerPiece.Third(Player.Green)
                )
            ),
        )

        runTestCases(testCases)
    }

    private fun runTestCases(testCases: List<TestCase>) {
        for (testCase in testCases) {
            val gameState = GameState(
                positions = testCase.positions,
                currentPlayer = Player.Green,
                players = setOf(Player.Green, Player.Red, Player.Blue, Player.Yellow),
                turnStatus = testCase.turnStatus,
                dice = Dice(diceNumber = testCase.diceNumber),
                error = null
            )

            val actual = sut.possibleMoves(gameState)
            assertEquals(testCase.expectedMoves, actual)
        }
    }

    data class TestCase(
        val positions: Map<Position, PlayerPiece>,
        val currentPlayer: Player,
        val turnStatus: TurnStatus,
        val diceNumber: Int,
        val expectedMoves: Set<PlayerPiece>
    )
}