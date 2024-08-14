package com.toybrokers.ludo.core.application

import com.toybrokers.ludo.core.domain.entities.GameState
import com.toybrokers.ludo.core.domain.events.GameEvent
import com.toybrokers.ludo.core.domain.interfaces.DiceRolledHandler
import com.toybrokers.ludo.core.domain.interfaces.GameEventManager
import com.toybrokers.ludo.core.domain.interfaces.PieceMovedHandler
import com.toybrokers.ludo.di.Koin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.get

class GameEventDefaultManager(
    private val initialState: GameState,
    private val diceRolledHandler: DiceRolledHandler = Koin.get(),
    private val pieceMovedHandler: PieceMovedHandler = Koin.get(),
    private val eventStack: MutableList<GameEvent> = mutableListOf(),
    private val _currentState: MutableStateFlow<GameState> = MutableStateFlow(initialState),
): GameEventManager {
    override fun addEvent(event: GameEvent) {
        eventStack.add(event)
        _currentState.tryEmit(applyEvent(_currentState.value, event))
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
            applyEvent(state, event)
        }
    }

    private fun applyEvent(state: GameState, event: GameEvent): GameState {
        return when(event) {
            is GameEvent.DiceRolled -> diceRolledHandler.handle(event, state)
            is GameEvent.PieceMoved -> pieceMovedHandler.handle(event, state)
        }
    }
}