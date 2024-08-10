package com.toybrokers.ludo.presentation.viewmodels

import com.toybrokers.ludo.domain.interfaces.GameEventManager
import com.toybrokers.ludo.domain.entities.GameEvent
import com.toybrokers.ludo.domain.entities.GameState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GameViewModel(private val gameEventManager: GameEventManager) {
    private val _gameState = MutableStateFlow(gameEventManager.getCurrentState())
    val gameState: StateFlow<GameState> get() = _gameState

    fun addEvent(event: GameEvent) {
        val newState = gameEventManager.addEvent(event)
        _gameState.value = newState
    }

    fun undo() {
        val newState = gameEventManager.undo()
        _gameState.value = newState
    }
}