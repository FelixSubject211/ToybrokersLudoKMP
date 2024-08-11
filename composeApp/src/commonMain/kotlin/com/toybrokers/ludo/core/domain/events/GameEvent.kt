package com.toybrokers.ludo.core.domain.events

import com.toybrokers.ludo.core.domain.entities.Dice
import com.toybrokers.ludo.core.domain.entities.PlayerPiece

sealed class GameEvent {
    data class PieceMoved(val piece: PlayerPiece) : GameEvent()
    data class DiceRolled(val dice: Dice): GameEvent()
}