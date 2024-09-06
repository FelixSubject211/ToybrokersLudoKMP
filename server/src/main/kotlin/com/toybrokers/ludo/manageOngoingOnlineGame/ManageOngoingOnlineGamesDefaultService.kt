package com.toybrokers.ludo.manageOngoingOnlineGame

import com.toybrokers.ludo.domain.entities.GameState
import com.toybrokers.ludo.domain.events.GameEvent
import com.toybrokers.ludo.network.manageOngoingOnlineGamesService.ManageOngoingOnlineGamesService
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext

class ManageOngoingOnlineGamesDefaultService(
    override val coroutineContext: CoroutineContext,
    private val manageOngoingOnlineGamesRepository: ManageOngoingOnlineGamesDefaultRepository
): ManageOngoingOnlineGamesService {
    override suspend fun addEvent(clientId: String, gameId: String, event: GameEvent) {
        manageOngoingOnlineGamesRepository.addEvent(clientId, gameId, event)
    }

    override suspend fun undo(clientId: String, gameId: String) {
        manageOngoingOnlineGamesRepository.undo(clientId, gameId)
    }

    override suspend fun currentState(clientId: String, gameId: String): StateFlow<GameState> {
        return manageOngoingOnlineGamesRepository.currentState(clientId, gameId)
    }
}