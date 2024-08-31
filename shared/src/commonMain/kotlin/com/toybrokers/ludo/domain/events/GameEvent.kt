package com.toybrokers.ludo.domain.events

import com.toybrokers.ludo.domain.entities.Dice
import com.toybrokers.ludo.domain.entities.PlayerPiece
import kotlinx.serialization.Serializable

@Serializable
sealed class GameEvent {
    @Serializable
    data class PieceMoved(val piece: PlayerPiece) : GameEvent()
    @Serializable
    data class DiceRolled(val dice: Dice): GameEvent()
}