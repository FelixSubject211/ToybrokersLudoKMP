package com.toybrokers.ludo.core.application

import com.toybrokers.ludo.core.domain.entities.GameState
import com.toybrokers.ludo.core.domain.events.GameEvent
import com.toybrokers.ludo.core.domain.interfaces.GameEventManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameEventDefaultManager(
    private val initialState: GameState,
    private val eventStack: MutableList<GameEvent> = mutableListOf(),
    private val _currentState: MutableStateFlow<GameState> = MutableStateFlow(initialState)
): GameEventManager {
    override fun addEvent(event: GameEvent) {
        eventStack.add(event)
        _currentState.tryEmit(_currentState.value.apply(event))
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