package com.toybrokers.ludo.network.manageOpenOnlineGamesService

import com.toybrokers.ludo.domain.entities.Player
import kotlinx.serialization.Serializable

@Serializable
sealed class ManageOpenOnlineGamesServiceJoinGameResult {
    @Serializable
    data object Success :  ManageOpenOnlineGamesServiceJoinGameResult()
    @Serializable
    data class GameNotFound(val gameId: String) : ManageOpenOnlineGamesServiceJoinGameResult()
    @Serializable
    data class PlayerAlreadyInGame(val clientId: String) :  ManageOpenOnlineGamesServiceJoinGameResult()
    @Serializable
    data class DuplicatePlayer(val player: Player) :  ManageOpenOnlineGamesServiceJoinGameResult()
}