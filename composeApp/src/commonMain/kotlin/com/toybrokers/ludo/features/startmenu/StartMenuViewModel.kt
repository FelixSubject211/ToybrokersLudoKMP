package com.toybrokers.ludo.features.startmenu

import com.toybrokers.ludo.Navigator
import com.toybrokers.ludo.core.application.GameEventDefaultManager
import com.toybrokers.ludo.core.application.Opponent
import com.toybrokers.ludo.core.application.TurnDefaultGatekeeper
import com.toybrokers.ludo.core.domain.entities.GameState
import com.toybrokers.ludo.core.domain.entities.Player
import com.toybrokers.ludo.features.game.GameViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class StartMenuViewModel {
    sealed class PlayerState {
        data object Inactive : PlayerState()
        data object ControlledByComputer : PlayerState()
        data object ControlledByHuman : PlayerState()
    }

    private val _playersState = MutableStateFlow(
        mapOf<Player, PlayerState>(
            Player.Red to PlayerState.Inactive,
            Player.Blue to PlayerState.Inactive,
            Player.Green to PlayerState.Inactive,
            Player.Yellow to PlayerState.Inactive
        )
    )

    val playersState = _playersState.asStateFlow()

    fun playerStateIsValid(): Boolean {
        return playersState.value.any { it.value != PlayerState.Inactive }
    }

    fun editPlayerState(player: Player, playerState: PlayerState) {
        _playersState.tryEmit(
            _playersState.value
                .toMutableMap()
                .apply {
                    set(player, playerState)
                }
        )
    }

    private val activeOpponents = mutableListOf<Opponent>()

    fun startGame() {
        stopAllOpponents()

        val allPlayers = _playersState.value
            .filter { it.value != PlayerState.Inactive }
            .keys

        val controlledByComputer = _playersState.value
            .filter { it.value == PlayerState.ControlledByComputer }
            .keys

        val controlledByHuman = _playersState.value
            .filter { it.value == PlayerState.ControlledByHuman }
            .keys

        val gameState = GameState.initialState(allPlayers)
        val gameEventManager = GameEventDefaultManager(gameState)

        val turnGatekeeper = TurnDefaultGatekeeper(
            players = controlledByHuman,
            gameEventManager = gameEventManager
        )

        Navigator.defaultNavigator.navigateTo(
            Navigator.Screen.GameBoard(
                viewModel = GameViewModel(turnGatekeeper)
            )
        )

        controlledByComputer.forEach {
            Opponent(
                player = it,
                gameEventManager = gameEventManager
            ).start()
        }
    }

    protected fun finalize() {
        stopAllOpponents()
    }

    private fun stopAllOpponents() {
        activeOpponents.forEach { it.stop() }
        activeOpponents.clear()
    }
}