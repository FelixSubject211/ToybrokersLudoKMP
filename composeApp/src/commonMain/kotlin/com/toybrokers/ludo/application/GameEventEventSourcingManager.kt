package com.toybrokers.ludo.application

import com.toybrokers.ludo.domain.interfaces.GameEventManager
import com.toybrokers.ludo.domain.entities.GameEvent
import com.toybrokers.ludo.domain.entities.GameState
import com.toybrokers.ludo.domain.entities.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameEventEventSourcingManager(
    private val initialState: GameState,
    private val eventStack: MutableList<GameEvent> = mutableListOf(),
    private val _currentState: MutableStateFlow<GameState> = MutableStateFlow(initialState)
): GameEventManager {
    override fun addEvent(event: GameEvent) {
        eventStack.add(event)
        _currentState.tryEmit(getCurrentState())
    }

    override fun undo() {
        eventStack.removeLastOrNull()
        _currentState.tryEmit(getCurrentState())
    }

    override fun currentState(): StateFlow<GameState> {
        return _currentState.asStateFlow()
    }

    private fun getCurrentState(): GameState {
        return eventStack.fold(initialState) { state, event ->
            state.apply(event)
        }
    }
}