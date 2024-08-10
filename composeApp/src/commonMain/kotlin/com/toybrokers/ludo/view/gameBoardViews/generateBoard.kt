package com.toybrokers.ludo.view.gameBoardViews

import com.toybrokers.ludo.entities.GameState
import com.toybrokers.ludo.entities.Player
import com.toybrokers.ludo.entities.PlayerPiece
import com.toybrokers.ludo.entities.Position

fun generateBoard(gameState: GameState): List<List<GridItem>>{
    return listOf(
        listOf(
            GridItem.Home(Player.Red, gameState.playerPiece(Position.Home(PlayerPiece.First(Player.Red)))),
            GridItem.Empty,
            GridItem.Home(Player.Red, gameState.playerPiece(Position.Home(PlayerPiece.Second(Player.Red)))),
            GridItem.Empty,
            GridItem.Track(gameState.playerPiece(Position.Track(8))),
            GridItem.Track(gameState.playerPiece(Position.Track(9))),
            GridItem.Track(gameState.playerPiece(Position.Track(10)), startOwner = Player.Blue),
            GridItem.Empty,
            GridItem.Home(Player.Blue, gameState.playerPiece(Position.Home(PlayerPiece.First(Player.Blue)))),
            GridItem.Empty,
            GridItem.Home(Player.Blue, gameState.playerPiece(Position.Home(PlayerPiece.Second(Player.Blue)))),
        ),
        listOf(
            GridItem.Empty,
            GridItem.Empty,
            GridItem.Empty,
            GridItem.Empty,
            GridItem.Track(gameState.playerPiece(Position.Track(7))),
            GridItem.End(Player.Blue, gameState.playerPiece(Position.End(Player.Blue, 0))),
            GridItem.Track(gameState.playerPiece(Position.Track(11))),
            GridItem.Empty,
            GridItem.Empty,
            GridItem.Empty,
            GridItem.Empty,
        ),
        listOf(
            GridItem.Home(Player.Red, gameState.playerPiece(Position.Home(PlayerPiece.Third(Player.Red)))),
            GridItem.Empty,
            GridItem.Home(Player.Red, gameState.playerPiece(Position.Home(PlayerPiece.Fourth(Player.Red)))),
            GridItem.Empty,
            GridItem.Track(gameState.playerPiece(Position.Track(6))),
            GridItem.End(Player.Blue, gameState.playerPiece(Position.End(Player.Blue, 1))),
            GridItem.Track(gameState.playerPiece(Position.Track(12))),
            GridItem.Empty,
            GridItem.Home(Player.Blue, gameState.playerPiece(Position.Home(PlayerPiece.Third(Player.Blue)))),
            GridItem.Empty,
            GridItem.Home(Player.Blue, gameState.playerPiece(Position.Home(PlayerPiece.Fourth(Player.Blue)))),
        ),
        listOf(
            GridItem.Empty,
            GridItem.Empty,
            GridItem.Empty,
            GridItem.Empty,
            GridItem.Track(gameState.playerPiece(Position.Track(5))),
            GridItem.End(Player.Blue, gameState.playerPiece(Position.End(Player.Blue, 2))),
            GridItem.Track(gameState.playerPiece(Position.Track(13))),
            GridItem.Empty,
            GridItem.Empty,
            GridItem.Empty,
            GridItem.Empty,
        ),
        listOf(
            GridItem.Track(gameState.playerPiece(Position.Track(0)), startOwner = Player.Red),
            GridItem.Track(gameState.playerPiece(Position.Track(1))),
            GridItem.Track(gameState.playerPiece(Position.Track(2))),
            GridItem.Track(gameState.playerPiece(Position.Track(3))),
            GridItem.Track(gameState.playerPiece(Position.Track(4))),
            GridItem.End(Player.Blue, gameState.playerPiece(Position.End(Player.Blue, 3))),
            GridItem.Track(gameState.playerPiece(Position.Track(14))),
            GridItem.Track(gameState.playerPiece(Position.Track(15))),
            GridItem.Track(gameState.playerPiece(Position.Track(16))),
            GridItem.Track(gameState.playerPiece(Position.Track(17))),
            GridItem.Track(gameState.playerPiece(Position.Track(18))),
        ),
        listOf(
            GridItem.Track(gameState.playerPiece(Position.Track(39))),
            GridItem.End(Player.Red, gameState.playerPiece(Position.End(Player.Red, 0))),
            GridItem.End(Player.Red, gameState.playerPiece(Position.End(Player.Red, 1))),
            GridItem.End(Player.Red, gameState.playerPiece(Position.End(Player.Red, 2))),
            GridItem.End(Player.Red, gameState.playerPiece(Position.End(Player.Red, 3))),
            GridItem.Empty,
            GridItem.End(Player.Green, gameState.playerPiece(Position.End(Player.Green, 3))),
            GridItem.End(Player.Green, gameState.playerPiece(Position.End(Player.Green, 2))),
            GridItem.End(Player.Green, gameState.playerPiece(Position.End(Player.Green, 1))),
            GridItem.End(Player.Green, gameState.playerPiece(Position.End(Player.Green, 0))),
            GridItem.Track(gameState.playerPiece(Position.Track(19))),
        ),
        listOf(
            GridItem.Track(gameState.playerPiece(Position.Track(38))),
            GridItem.Track(gameState.playerPiece(Position.Track(37))),
            GridItem.Track(gameState.playerPiece(Position.Track(36))),
            GridItem.Track(gameState.playerPiece(Position.Track(35))),
            GridItem.Track(gameState.playerPiece(Position.Track(34))),
            GridItem.End(Player.Yellow, gameState.playerPiece(Position.End(Player.Yellow, 3))),
            GridItem.Track(gameState.playerPiece(Position.Track(24))),
            GridItem.Track(gameState.playerPiece(Position.Track(23))),
            GridItem.Track(gameState.playerPiece(Position.Track(22))),
            GridItem.Track(gameState.playerPiece(Position.Track(21))),
            GridItem.Track(gameState.playerPiece(Position.Track(20)), startOwner = Player.Green),
        ),
        listOf(
            GridItem.Empty,
            GridItem.Empty,
            GridItem.Empty,
            GridItem.Empty,
            GridItem.Track(gameState.playerPiece(Position.Track(33))),
            GridItem.End(Player.Yellow, gameState.playerPiece(Position.End(Player.Yellow, 2))),
            GridItem.Track(gameState.playerPiece(Position.Track(25))),
            GridItem.Empty,
            GridItem.Empty,
            GridItem.Empty,
            GridItem.Empty,
        ),
        listOf(
            GridItem.Home(Player.Yellow, gameState.playerPiece(Position.Home(PlayerPiece.First(Player.Yellow)))),
            GridItem.Empty,
            GridItem.Home(Player.Yellow, gameState.playerPiece(Position.Home(PlayerPiece.Second(Player.Yellow)))),
            GridItem.Empty,
            GridItem.Track(gameState.playerPiece(Position.Track(32))),
            GridItem.End(Player.Yellow, gameState.playerPiece(Position.End(Player.Yellow, 1))),
            GridItem.Track(gameState.playerPiece(Position.Track(26))),
            GridItem.Empty,
            GridItem.Home(Player.Green, gameState.playerPiece(Position.Home(PlayerPiece.First(Player.Green)))),
            GridItem.Empty,
            GridItem.Home(Player.Green, gameState.playerPiece(Position.Home(PlayerPiece.Second(Player.Green)))),
        ),
        listOf(
            GridItem.Empty,
            GridItem.Empty,
            GridItem.Empty,
            GridItem.Empty,
            GridItem.Track(gameState.playerPiece(Position.Track(31))),
            GridItem.End(Player.Yellow, gameState.playerPiece(Position.End(Player.Yellow, 0))),
            GridItem.Track(gameState.playerPiece(Position.Track(27))),
            GridItem.Empty,
            GridItem.Empty,
            GridItem.Empty,
            GridItem.Empty,
        ),
        listOf(
            GridItem.Home(
                Player.Yellow, gameState.playerPiece(
                    Position.Home(
                        PlayerPiece.Third(
                            Player.Yellow)))),
            GridItem.Empty,
            GridItem.Home(
                Player.Yellow, gameState.playerPiece(
                    Position.Home(
                        PlayerPiece.Fourth(
                            Player.Yellow)))),
            GridItem.Empty,
            GridItem.Track(gameState.playerPiece(Position.Track(30)), startOwner = Player.Yellow),
            GridItem.Track(gameState.playerPiece(Position.Track(29))),
            GridItem.Track(gameState.playerPiece(Position.Track(28))),
            GridItem.Empty,
            GridItem.Home(Player.Green, gameState.playerPiece(Position.Home(PlayerPiece.Third(Player.Green)))),
            GridItem.Empty,
            GridItem.Home(
                Player.Green, gameState.playerPiece(
                    Position.Home(
                        PlayerPiece.Fourth(
                            Player.Green)))),
        ),
    )
}

sealed class GridItem {
    data class Home(val player: Player, val playerPiece: PlayerPiece?) : GridItem()
    data class Track(val playerPiece: PlayerPiece?, val startOwner: Player? = null) : GridItem()
    data class End(val player: Player, val playerPiece: PlayerPiece?) : GridItem()
    data object Empty : GridItem()
}

private fun GameState.playerPiece(position: Position): PlayerPiece? {
    return positions[position]
}