package com.toybrokers.ludo.domain.entities

sealed class GameError {
    data object NotPlayersTurn : GameError()
    data object MustRollDice : GameError()
    data object MustMovePiece : GameError()
    data object InvalidMove : GameError()
}