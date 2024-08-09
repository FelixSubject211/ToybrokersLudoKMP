package com.toybrokers.ludo.entities

sealed class GameEventError {
    data object NotPlayersTurn : GameEventError()
    data object MustRollDice : GameEventError()
    data object MustMovePiece : GameEventError()
    data object InvalidMove : GameEventError()
}