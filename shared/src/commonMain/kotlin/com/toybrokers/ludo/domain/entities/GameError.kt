package com.toybrokers.ludo.domain.entities

import kotlinx.serialization.Serializable

@Serializable
sealed class GameError {
    @Serializable
    data object NotPlayersTurn : GameError()
    @Serializable
    data object MustRollDice : GameError()
    @Serializable
    data object MustMovePiece : GameError()
    @Serializable
    data object InvalidMove : GameError()
}