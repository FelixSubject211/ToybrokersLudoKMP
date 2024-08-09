package com.toybrokers.ludo.entities

sealed class GameEvent {
    data class PieceMoved(val piece: PlayerPiece) : GameEvent()
    data class DiceRolled(val diceNumber: Int): GameEvent()
}