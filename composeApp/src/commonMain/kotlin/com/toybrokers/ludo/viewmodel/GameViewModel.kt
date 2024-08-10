package com.toybrokers.ludo.viewmodel

import com.toybrokers.ludo.controllers.GameEventManager
import com.toybrokers.ludo.entities.GameEvent
import com.toybrokers.ludo.entities.GameState
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