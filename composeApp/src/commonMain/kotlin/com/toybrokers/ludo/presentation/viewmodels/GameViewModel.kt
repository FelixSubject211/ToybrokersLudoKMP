package com.toybrokers.ludo.presentation.viewmodels

import com.toybrokers.ludo.domain.interfaces.GameEventManager
import com.toybrokers.ludo.domain.entities.GameEvent
import com.toybrokers.ludo.domain.entities.GameState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GameViewModel(private val gameEventManager: GameEventManager) {
    val gameState: StateFlow<GameState> get() = gameEventManager.currentState()

    fun addEvent(event: GameEvent) {
        gameEventManager.addEvent(event)

    }

    fun undo() {
        gameEventManager.undo()
    }
}