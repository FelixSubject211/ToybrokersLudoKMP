package com.toybrokers.ludo.features.manageOpenOnlineGames

import com.toybrokers.ludo.Navigator
import com.toybrokers.ludo.domain.entities.GameState
import com.toybrokers.ludo.domain.entities.Player
import com.toybrokers.ludo.domain.entities.UUID
import com.toybrokers.ludo.domain.events.GameEvent
import com.toybrokers.ludo.domain.interfaces.TurnGatekeeper
import com.toybrokers.ludo.features.game.GameViewModel
import com.toybrokers.ludo.getHost
import com.toybrokers.ludo.main.BuildKonfig
import com.toybrokers.ludo.network.manageOngoingOnlineGamesService.ManageOngoingOnlineGamesService
import com.toybrokers.ludo.network.manageOpenOnlineGamesService.ManageOpenOnlineGamesService
import com.toybrokers.ludo.network.manageOpenOnlineGamesService.ManageOpenOnlineGamesServiceJoinGameResult
import com.toybrokers.ludo.network.manageOpenOnlineGamesService.ManageOpenOnlineGamesServiceResponse
import io.ktor.client.HttpClient
import io.ktor.http.encodedPath
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.rpc.serialization.json
import kotlinx.rpc.streamScoped
import kotlinx.rpc.transport.ktor.client.installRPC
import kotlinx.rpc.transport.ktor.client.rpc
import kotlinx.rpc.transport.ktor.client.rpcConfig
import kotlinx.rpc.withService

class ManageOpenOnlineGamesViewModel(
    private val navigator: Navigator
) {
    private var service: ManageOpenOnlineGamesService? = null
    private val _games = MutableStateFlow(ManageOpenOnlineGamesServiceResponse.PendingOnlineGames(emptyList()))
    private val _error = MutableStateFlow<Throwable?>(null)
    private val _joinGameError = MutableStateFlow<String?>(null)
    private val _selectedPlayer = MutableStateFlow<Player>(Player.Red)
    private val _showPlayerSelector = MutableStateFlow(false)
    private val _selectedGameId = MutableStateFlow<String?>(null)
    private val clientId = UUID.randomUUID().toString()

    init {
        initService()
    }

    val games = _games.asStateFlow()
    val error = _error.asStateFlow()
    val joinGameError = _joinGameError.asStateFlow()
    val showPlayerSelector = _showPlayerSelector.asStateFlow()
    val selectedGameId = _selectedGameId.asStateFlow()

    fun closeJoinGameError() {
        _joinGameError.value = null
    }

    fun setSelectedPlayer(player: Player) {
        _selectedPlayer.value = player
    }

    fun setShowPlayerSelector(show: Boolean) {
        _showPlayerSelector.value = show
    }

    fun setSelectedGameId(gameId: String?) {
        _selectedGameId.value = gameId
    }

    fun openNewGame() {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                service!!.addGame(clientId, _selectedPlayer.value)
            } catch (e: Throwable) {
                _error.value = e
            } finally {
                _showPlayerSelector.value = false
            }
        }
    }

    fun joinGame() {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val result = service!!.joinGame(
                    clientId,
                    _selectedGameId.value!!,
                    _selectedPlayer.value
                )
                if (result !is ManageOpenOnlineGamesServiceJoinGameResult.Success) {
                    _joinGameError.value = result.toString()
                }
            } catch (e: Throwable) {
                _error.value = e
            } finally {
                _showPlayerSelector.value = false
            }
        }
    }

    fun initService() {
        _error.value = null
        val client = HttpClient {
            installRPC()
        }

        CoroutineScope(Dispatchers.Default).launch {
            try {
                service = client.rpc {
                    url {
                        host = BuildKonfig.getHost()
                        port = BuildKonfig.port
                        encodedPath = "/ManageOpenOnlineGamesService"
                    }

                    rpcConfig {
                        serialization {
                            json {
                                allowStructuredMapKeys = true
                            }
                        }
                    }
                }.withService()

                streamScoped {
                    service?.listOpenGames(clientId)?.collect {
                        when(it) {
                            is ManageOpenOnlineGamesServiceResponse.PendingOnlineGames -> {
                                _games.value = it
                            }
                            is ManageOpenOnlineGamesServiceResponse.OngoingOnlineGame ->  {
                                val ongoingGamesService: ManageOngoingOnlineGamesService = client.rpc {
                                    url {
                                        host = BuildKonfig.getHost()
                                        port = BuildKonfig.port
                                        encodedPath = "/ManageOngoingOnlineGamesService"
                                    }

                                    rpcConfig {
                                        serialization {
                                            json {
                                                allowStructuredMapKeys = true
                                            }
                                        }
                                    }
                                }.withService()

                                val coroutineScope = CoroutineScope(Dispatchers.Default)

                                navigator.navigateTo(Navigator.Screen.GameBoard(
                                    viewModel = GameViewModel(object : TurnGatekeeper {
                                        override fun addEvent(event: GameEvent) {
                                            coroutineScope.launch {
                                                ongoingGamesService.addEvent(clientId, it.gameId, event)
                                            }
                                        }

                                        override fun undo() {
                                            coroutineScope.launch {
                                                ongoingGamesService.undo(clientId, it.gameId)
                                            }
                                        }

                                        override fun currentState(): StateFlow<GameState> {
                                            val stateFlow = MutableStateFlow(
                                                GameState.initialState(setOf(Player.Red))
                                            )

                                            coroutineScope.launch {
                                                streamScoped {
                                                    ongoingGamesService.currentState(clientId, it.gameId).collect { gameState ->
                                                        stateFlow.value = gameState
                                                    }
                                                }
                                            }

                                            return stateFlow
                                        }
                                    },
                                    navigator = navigator
                                    )
                                ))
                            }
                        }
                    }
                }
            } catch (e: Throwable) {
                _error.value = e
            }
        }
    }
}