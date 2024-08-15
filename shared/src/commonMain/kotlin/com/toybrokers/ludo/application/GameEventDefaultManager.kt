package com.toybrokers.ludo.application


import com.toybrokers.ludo.domain.entities.GameState
import com.toybrokers.ludo.domain.events.GameEvent
import com.toybrokers.ludo.domain.interfaces.DiceRolledHandler
import com.toybrokers.ludo.domain.interfaces.GameEventManager
import com.toybrokers.ludo.domain.interfaces.PieceMovedHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class GameEventDefaultManager(
    private val initialState: GameState,
    private val diceRolledHandler: DiceRolledHandler,
    private val pieceMovedHandler: PieceMovedHandler,
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