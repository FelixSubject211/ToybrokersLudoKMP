package com.toybrokers.ludo.network.manageOpenOnlineGamesService

import com.toybrokers.ludo.domain.entities.Player
import kotlinx.serialization.Serializable

@Serializable
sealed class ManageOpenOnlineGamesServiceResponse {

    @Serializable
    data class PendingOnlineGames(
        val games: List<Game>,
    ) : ManageOpenOnlineGamesServiceResponse() {
        @Serializable
        data class Game(
            val gameId: String,
            val players: List<Player>,
        )
    }

    @Serializable
    data class OngoingOnlineGame(
        val gameId: String,
    ) : ManageOpenOnlineGamesServiceResponse()
}