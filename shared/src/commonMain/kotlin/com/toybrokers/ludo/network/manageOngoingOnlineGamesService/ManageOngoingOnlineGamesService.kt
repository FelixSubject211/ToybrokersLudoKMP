package com.toybrokers.ludo.network.manageOngoingOnlineGamesService

import com.toybrokers.ludo.domain.entities.GameState
import com.toybrokers.ludo.domain.events.GameEvent
import kotlinx.coroutines.flow.StateFlow
import kotlinx.rpc.RPC

interface ManageOngoingOnlineGamesService: RPC {
    suspend fun addEvent(clientId: String, gameId: String, event: GameEvent)
    suspend fun undo(clientId: String, gameId: String)
    suspend fun currentState(clientId: String, gameId: String): StateFlow<GameState>
}