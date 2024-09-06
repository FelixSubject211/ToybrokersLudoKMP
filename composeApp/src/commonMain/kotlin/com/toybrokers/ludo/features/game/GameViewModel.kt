package com.toybrokers.ludo.features.game

import com.toybrokers.ludo.Navigator
import com.toybrokers.ludo.domain.entities.GameState
import com.toybrokers.ludo.domain.events.GameEvent
import com.toybrokers.ludo.domain.interfaces.TurnGatekeeper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel(
    private val turnGatekeeper: TurnGatekeeper,
    private val navigator: Navigator
) {
    val gameState: StateFlow<GameState> get() = turnGatekeeper.currentState()
    private val _menuExpanded = MutableStateFlow(false)
    val menuExpanded = _menuExpanded.asStateFlow()

    fun addEvent(event: GameEvent) {
        turnGatekeeper.addEvent(event)
    }

    fun toggleMenu() {
        _menuExpanded.value = !_menuExpanded.value
    }

    fun onDismissMenu() {
        _menuExpanded.value = false
    }

    fun undo() {
        turnGatekeeper.undo()
        onDismissMenu()
    }

    fun onEndGame() {
        navigator.goBack()
        onDismissMenu()
    }
}