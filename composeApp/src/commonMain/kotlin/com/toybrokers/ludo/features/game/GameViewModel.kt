package com.toybrokers.ludo.features.game

import com.toybrokers.ludo.Navigator
import com.toybrokers.ludo.core.domain.entities.GameEvent
import com.toybrokers.ludo.core.domain.entities.GameState
import com.toybrokers.ludo.core.domain.interfaces.TurnGatekeeper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel(private val turnGatekeeper: TurnGatekeeper) {
    val gameState: StateFlow<GameState> get() = turnGatekeeper.currentState()
    private val _menuExpanded = MutableStateFlow(false)
    val menuExpanded = _menuExpanded.asStateFlow()

    fun addEvent(event: GameEvent) {
        turnGatekeeper.addEvent(event)

    }

    fun undo() {
        turnGatekeeper.undo()
    }

    fun toggleMenu() {
        _menuExpanded.value = !_menuExpanded.value
    }

    fun onDismissMenu() {
        _menuExpanded.value = false
    }

    fun onEndGame() {
        Navigator.defaultNavigator.goBack()
    }
}