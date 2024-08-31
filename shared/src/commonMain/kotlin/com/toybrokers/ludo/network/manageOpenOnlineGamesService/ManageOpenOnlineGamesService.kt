package com.toybrokers.ludo.network.manageOpenOnlineGamesService

import com.toybrokers.ludo.domain.entities.Player
import kotlinx.coroutines.flow.Flow
import kotlinx.rpc.RPC

interface ManageOpenOnlineGamesService: RPC {
    suspend fun listOpenGames(clientId: String): Flow<ManageOpenOnlineGamesServiceResponse>

    suspend fun addGame(clientId: String, player: Player)

    suspend fun joinGame(
        clientId: String,
        gameId: String,
        player: Player
    ): ManageOpenOnlineGamesServiceJoinGameResult
}