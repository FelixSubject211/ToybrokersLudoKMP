package com.toybrokers.ludo.manageOpenOnlineGame

import com.toybrokers.ludo.domain.entities.Player
import com.toybrokers.ludo.network.manageOpenOnlineGamesService.ManageOpenOnlineGamesServiceJoinGameResult
import com.toybrokers.ludo.network.manageOpenOnlineGamesService.ManageOpenOnlineGamesServiceResponse
import kotlinx.coroutines.flow.Flow

interface ManageOpenOnlineGamesRepository {
    suspend fun listOpenGames(clientId: String): Flow<ManageOpenOnlineGamesServiceResponse>

    suspend fun addGame(clientId: String, player: Player)

    suspend fun joinGame(
        clientId: String,
        gameId: String,
        player: Player
    ): ManageOpenOnlineGamesServiceJoinGameResult
}