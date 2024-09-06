package com.toybrokers.ludo.manageOngoingOnlineGame

import com.toybrokers.ludo.application.GameEventDefaultManager
import com.toybrokers.ludo.application.TurnDefaultGatekeeper
import com.toybrokers.ludo.domain.entities.GameState
import com.toybrokers.ludo.domain.entities.Player
import com.toybrokers.ludo.domain.events.GameEvent
import com.toybrokers.ludo.domain.handlers.DefaultDiceRolledHandler
import com.toybrokers.ludo.domain.handlers.DefaultMoveCalculator
import com.toybrokers.ludo.domain.handlers.DefaultNextPlayerCalculator
import com.toybrokers.ludo.domain.handlers.DefaultPieceMovedHandler
import com.toybrokers.ludo.domain.interfaces.TurnGatekeeper
import kotlinx.coroutines.flow.StateFlow

class ManageOngoingOnlineGamesDefaultRepository: ManageOngoingOnlineGamesRepository {
    private var ongoingGames = emptyMap<String, OngoingGame>()

    override suspend fun addEvent(clientId: String, gameId: String, event: GameEvent) {
        ongoingGames[gameId]?.turnGatekeepers?.get(clientId)?.let {
            it.addEvent(event)
        }
    }

    override suspend fun undo(clientId: String, gameId: String) {
        ongoingGames[gameId]?.turnGatekeepers?.get(clientId)?.let {
            it.undo()
        }
    }

    override suspend fun currentState(clientId: String, gameId: String): StateFlow<GameState> {
        ongoingGames[gameId]?.turnGatekeepers?.get(clientId)?.let {
            return it.currentState()
        } ?: throw IllegalStateException()
    }

    override fun addGame(gameId: String, players: Map<String, Player>) {
        val gameEventManager = GameEventDefaultManager(
            initialState = GameState.initialState(players.values.toSet()),
            diceRolledHandler = DefaultDiceRolledHandler(
                moveCalculator = DefaultMoveCalculator(),
                nextPlayerCalculator = DefaultNextPlayerCalculator()
            ),
            pieceMovedHandler = DefaultPieceMovedHandler(
                moveCalculator = DefaultMoveCalculator(),
                nextPlayerCalculator = DefaultNextPlayerCalculator()
            )
        )

        ongoingGames = ongoingGames.plus(
            pair = gameId to OngoingGame(
                gameId = gameId,
                turnGatekeepers = players.mapValues {
                    TurnDefaultGatekeeper(
                        players = setOf(it.value),
                        gameEventManager = gameEventManager
                    )
                }
            )
        )
    }

    data class OngoingGame(
        val gameId: String,
        val turnGatekeepers: Map<String, TurnGatekeeper>
    )
}