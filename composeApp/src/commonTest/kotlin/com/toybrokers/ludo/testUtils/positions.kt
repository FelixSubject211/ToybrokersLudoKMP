package com.toybrokers.ludo.testUtils

import com.toybrokers.ludo.core.domain.entities.Player
import com.toybrokers.ludo.core.domain.entities.PlayerPiece
import com.toybrokers.ludo.core.domain.entities.Position

fun positions(
    playersInHome: Set<Player> = emptySet(),
    playersInHomeAndTrackAndEnd: Set<Player> = emptySet(),
    playersInEnd: Set<Player> = emptySet()
): Map<Position, PlayerPiece> {
    val positionsInHome = playersInHome
        .flatMap { player ->
            PlayerPiece.all(player).map { piece -> piece.home() to piece }
        }
        .toMap()

    val positionsInHomeAndTrackAndEnd = playersInHomeAndTrackAndEnd
        .flatMap { player ->
            PlayerPiece.all(player).map { piece ->
                when(piece) {
                    is PlayerPiece.First -> piece.home() to piece
                    is PlayerPiece.Fourth -> piece.home() to piece
                    is PlayerPiece.Second -> piece.owner.start() to piece
                    is PlayerPiece.Third -> Position.End(player, 3) to piece
                }
            }
        }
        .toMap()

    val positionsInEnd = playersInEnd
        .flatMap { player ->
            PlayerPiece.all(player).map { piece ->
                when(piece) {
                    is PlayerPiece.First -> Position.End(player, 0) to piece
                    is PlayerPiece.Fourth -> Position.End(player, 1) to piece
                    is PlayerPiece.Second -> Position.End(player, 2) to piece
                    is PlayerPiece.Third -> Position.End(player, 3) to piece
                }
            }
        }
        .toMap()

    return positionsInHome + positionsInHomeAndTrackAndEnd + positionsInEnd
}