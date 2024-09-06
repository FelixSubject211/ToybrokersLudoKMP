package com.toybrokers.ludo.manageOngoingOnlineGame

import com.toybrokers.ludo.domain.entities.GameState
import com.toybrokers.ludo.domain.entities.Player
import com.toybrokers.ludo.domain.events.GameEvent
import kotlinx.coroutines.flow.StateFlow

interface ManageOngoingOnlineGamesRepository {
    suspend fun addEvent(clientId: String, gameId: String, event: GameEvent)

    suspend fun undo(clientId: String, gameId: String)

    suspend fun currentState(clientId: String, gameId: String): StateFlow<GameState>

    fun addGame(gameId: String, players: Map<String, Player>)
}