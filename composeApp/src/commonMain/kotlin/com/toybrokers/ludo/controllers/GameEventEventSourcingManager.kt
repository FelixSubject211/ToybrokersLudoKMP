package com.toybrokers.ludo.controllers

import com.toybrokers.ludo.entities.GameEvent
import com.toybrokers.ludo.entities.GameState
import com.toybrokers.ludo.entities.Player

class GameEventEventSourcingManager: GameEventManager {
    private val eventStack: MutableList<GameEvent> = mutableListOf()
    private val initialState = GameState.initialState(Player.Green, Player.Yellow, Player.Red, Player.Blue)

    override fun addEvent(event: GameEvent): GameState {
        eventStack.add(event)
        return getCurrentState()
    }

    override fun getCurrentState(): GameState {
        return eventStack.fold(initialState) { state, event ->
            state.apply(event)
        }
    }
}