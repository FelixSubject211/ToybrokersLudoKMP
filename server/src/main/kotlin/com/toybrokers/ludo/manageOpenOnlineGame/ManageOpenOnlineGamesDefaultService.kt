package com.toybrokers.ludo.manageOpenOnlineGame

import com.toybrokers.ludo.domain.entities.Player
import com.toybrokers.ludo.network.manageOpenOnlineGamesService.ManageOpenOnlineGamesService
import com.toybrokers.ludo.network.manageOpenOnlineGamesService.ManageOpenOnlineGamesServiceJoinGameResult
import com.toybrokers.ludo.network.manageOpenOnlineGamesService.ManageOpenOnlineGamesServiceResponse
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

class ManageOpenOnlineGamesDefaultService(
    override val coroutineContext: CoroutineContext,
    private val manageOpenOnlineGamesRepository: ManageOpenOnlineGamesRepository
) : ManageOpenOnlineGamesService {

    override suspend fun listOpenGames(clientId: String): Flow<ManageOpenOnlineGamesServiceResponse> {
        return manageOpenOnlineGamesRepository.listOpenGames(clientId)
    }

    override suspend fun addGame(clientId: String, player: Player) {
        manageOpenOnlineGamesRepository.addGame(clientId, player)
    }

    override suspend fun joinGame(
        clientId: String,
        gameId: String,
        player: Player
    ): ManageOpenOnlineGamesServiceJoinGameResult {
        return manageOpenOnlineGamesRepository.joinGame(clientId, gameId, player)
    }
}