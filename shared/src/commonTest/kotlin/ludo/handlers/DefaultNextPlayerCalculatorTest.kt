package ludo.handlers

import com.toybrokers.ludo.domain.entities.Dice
import com.toybrokers.ludo.domain.entities.GameState
import com.toybrokers.ludo.domain.entities.Player
import com.toybrokers.ludo.domain.entities.PlayerPiece
import com.toybrokers.ludo.domain.entities.Position
import com.toybrokers.ludo.domain.entities.TurnStatus
import com.toybrokers.ludo.domain.handlers.DefaultNextPlayerCalculator
import com.toybrokers.ludo.domain.interfaces.NextPlayerCalculator
import ludo.testUtils.positions
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultNextPlayerCalculatorTest {

    private val sut = DefaultNextPlayerCalculator()

    @Test
    fun shouldReturnTheNextPlayerFromPlayersOfTheGame() {
        val testCases = listOf(
            TestCase(
                positions = positions(
                    playersInHome = setOf(Player.Blue, Player.Green, Player.Yellow, Player.Red)
                ),
                currentPlayer = Player.Blue,
                players = setOf(Player.Blue, Player.Green, Player.Yellow, Player.Red),
                expectedResult = NextPlayerCalculator.Result.NextPlayer(Player.Green)
            ),
            TestCase(
                positions = positions(
                    playersInHomeAndTrackAndEnd = setOf(Player.Blue, Player.Green, Player.Yellow, Player.Red)
                ),
                currentPlayer = Player.Green,
                players = setOf(Player.Blue, Player.Green, Player.Yellow, Player.Red),
                expectedResult = NextPlayerCalculator.Result.NextPlayer(Player.Yellow)
            ),
            TestCase(
                positions = positions(
                    playersInHome = setOf(Player.Blue, Player.Yellow, Player.Red)
                ),
                currentPlayer = Player.Yellow,
                players = setOf(Player.Blue, Player.Yellow, Player.Red),
                expectedResult = NextPlayerCalculator.Result.NextPlayer(Player.Red)
            ),
            TestCase(
                positions = positions(
                    playersInHomeAndTrackAndEnd = setOf(Player.Blue, Player.Red)
                ),
                currentPlayer = Player.Red,
                players = setOf(Player.Blue, Player.Red),
                expectedResult = NextPlayerCalculator.Result.NextPlayer(Player.Blue)
            ),
            TestCase(
                positions = positions(playersInHome = setOf(Player.Blue)),
                currentPlayer = Player.Blue,
                players = setOf(Player.Blue),
                expectedResult = NextPlayerCalculator.Result.NextPlayer(Player.Blue)
            ),
        )

        runTestCases(testCases)
    }

    @Test
    fun shouldIgnorePlayerWithAllPlayerPiecesInEnd() {
        val testCases = listOf(
            TestCase(
                positions = positions(
                    playersInHome = setOf(Player.Blue, Player.Yellow, Player.Red),
                    playersInEnd = setOf(Player.Green)
                ),
                currentPlayer = Player.Blue,
                players = setOf(Player.Blue, Player.Green, Player.Yellow, Player.Red),
                expectedResult = NextPlayerCalculator.Result.NextPlayer(Player.Yellow)
            ),
            TestCase(
                positions = positions(
                    playersInHomeAndTrackAndEnd = setOf(Player.Blue, Player.Green, Player.Red),
                    playersInEnd = setOf(Player.Yellow)
                ),
                currentPlayer = Player.Green,
                players = setOf(Player.Blue, Player.Green, Player.Yellow, Player.Red),
                expectedResult = NextPlayerCalculator.Result.NextPlayer(Player.Red)
            ),
        )

        runTestCases(testCases)
    }

    @Test
    fun shouldReturnGameIsFinishIfAllPlayersInEnd() {
        val testCases = listOf(
            TestCase(
                positions = positions(
                    playersInEnd = setOf(Player.Blue, Player.Green, Player.Yellow, Player.Red)
                ),
                currentPlayer = Player.Blue,
                players = setOf(Player.Blue, Player.Green, Player.Yellow, Player.Red),
                expectedResult = NextPlayerCalculator.Result.GameIsFinish
            ),
        )

        runTestCases(testCases)
    }

    private fun runTestCases(testCases: List<TestCase>) {
        for (testCase in testCases) {
            val gameState = GameState(
                positions = testCase.positions,
                currentPlayer = testCase.currentPlayer,
                players = testCase.players,
                turnStatus = TurnStatus.Move,
                dice = Dice(diceNumber = 1),
                error = null
            )

            val actual = sut.nextPlayer(gameState)
            assertEquals(testCase.expectedResult, actual)
        }
    }

    data class TestCase(
        val positions: Map<Position, PlayerPiece>,
        val currentPlayer: Player,
        val players: Set<Player>,
        val expectedResult: NextPlayerCalculator.Result
    )
}