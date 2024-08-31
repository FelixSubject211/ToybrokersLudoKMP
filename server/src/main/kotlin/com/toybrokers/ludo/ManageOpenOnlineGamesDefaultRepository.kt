package com.toybrokers.ludo

import com.toybrokers.ludo.domain.entities.Player
import com.toybrokers.ludo.domain.entities.UUID
import com.toybrokers.ludo.network.manageOpenOnlineGamesService.ManageOpenOnlineGamesServiceJoinGameResult
import com.toybrokers.ludo.network.manageOpenOnlineGamesService.ManageOpenOnlineGamesServiceResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class ManageOpenOnlineGamesDefaultRepository(
    private val manageOngoingOnlineGamesRepository: ManageOngoingOnlineGamesRepository
): ManageOpenOnlineGamesRepository {
    private val pendingGames = MutableStateFlow(emptyMap<String, PendingGame>())

    override suspend fun listOpenGames(clientId: String): Flow<ManageOpenOnlineGamesServiceResponse> {
        return pendingGames.map { games ->
            games.values.firstOrNull { game ->
                game.players.any { it.first == clientId } &&
                        game.players.size >= 4
            }?.let {
                manageOngoingOnlineGamesRepository.addGame(
                    gameId = it.gameId,
                    players = it.players.associateBy { it.first }.mapValues { it.value.second }
                )
                pendingGames.value = pendingGames.value.minus(it.gameId)

                ManageOpenOnlineGamesServiceResponse.OngoingOnlineGame(it.gameId)
            } ?: run {
                ManageOpenOnlineGamesServiceResponse.PendingOnlineGames(
                    games = games.map { game ->
                        ManageOpenOnlineGamesServiceResponse.PendingOnlineGames.Game(
                            gameId = game.key,
                            players = game.value.players.map { it.second }
                        )
                    }
                )
            }
        }
    }

    override suspend fun addGame(clientId: String, player: Player) {
        val newGame = PendingGame(
            gameId = UUID.randomUUID().toString(),
            players = listOf(Pair(clientId, player))
        )
        pendingGames.emit(pendingGames.value.plus(newGame.gameId to newGame))
    }

    override suspend fun joinGame(
        clientId: String,
        gameId: String,
        player: Player
    ): ManageOpenOnlineGamesServiceJoinGameResult {
        val game = pendingGames.value[gameId] ?:
            return ManageOpenOnlineGamesServiceJoinGameResult.GameNotFound(gameId)
        if (game.players.any { it.first == clientId }) {
            return ManageOpenOnlineGamesServiceJoinGameResult.PlayerAlreadyInGame(clientId)
        }
        if (game.players.any { it.second == player }) {
            return ManageOpenOnlineGamesServiceJoinGameResult.DuplicatePlayer(player)
        }
        pendingGames.emit(pendingGames.value.plus(gameId to PendingGame(
            gameId = gameId,
            players = game.players.plus(clientId to player)
        )))
        return ManageOpenOnlineGamesServiceJoinGameResult.Success
    }

    data class PendingGame(
        val gameId: String,
        val players: List<Pair<String, Player>>
    )
}