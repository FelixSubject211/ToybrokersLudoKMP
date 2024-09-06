package com.toybrokers.ludo.features.startmenu

import com.toybrokers.ludo.Navigator
import com.toybrokers.ludo.application.DefaultOpponent
import com.toybrokers.ludo.application.GameEventDefaultManager
import com.toybrokers.ludo.application.TurnDefaultGatekeeper
import com.toybrokers.ludo.domain.entities.GameState
import com.toybrokers.ludo.domain.entities.Player
import com.toybrokers.ludo.domain.handlers.DefaultDiceRolledHandler
import com.toybrokers.ludo.domain.handlers.DefaultMoveCalculator
import com.toybrokers.ludo.domain.handlers.DefaultNextPlayerCalculator
import com.toybrokers.ludo.domain.handlers.DefaultPieceMovedHandler
import com.toybrokers.ludo.domain.interfaces.Opponent
import com.toybrokers.ludo.features.game.GameViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class StartMenuViewModel(
    private val navigator: Navigator
) {
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

    private val activeOpponents = mutableListOf<Opponent>()

    val playersState = _playersState.asStateFlow()

    fun editPlayerState(player: Player, playerState: PlayerState) {
        _playersState.tryEmit(
            _playersState.value
                .toMutableMap()
                .apply {
                    set(player, playerState)
                }
        )
    }

    fun playerStateIsValid(): Boolean {
        return playersState.value.any { it.value != PlayerState.Inactive }
    }

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
        val gameEventManager = GameEventDefaultManager(
            initialState = gameState,
            diceRolledHandler = DefaultDiceRolledHandler(
                moveCalculator = DefaultMoveCalculator(),
                nextPlayerCalculator = DefaultNextPlayerCalculator()
            ),
            pieceMovedHandler = DefaultPieceMovedHandler(
                moveCalculator = DefaultMoveCalculator(),
                nextPlayerCalculator = DefaultNextPlayerCalculator()
            )
        )

        val turnGatekeeper = TurnDefaultGatekeeper(
            players = controlledByHuman,
            canUndo = true,
            gameEventManager = gameEventManager
        )

        navigator.navigateTo(
            Navigator.Screen.GameBoard(
                viewModel = GameViewModel(turnGatekeeper, navigator)
            )
        )

        controlledByComputer.forEach {
            DefaultOpponent(
                player = it,
                gameEventManager = gameEventManager,
                moveCalculator = DefaultMoveCalculator()
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