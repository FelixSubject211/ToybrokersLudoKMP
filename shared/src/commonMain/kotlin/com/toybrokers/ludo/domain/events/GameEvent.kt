package com.toybrokers.ludo.domain.events

import com.toybrokers.ludo.domain.entities.Dice
import com.toybrokers.ludo.domain.entities.PlayerPiece


sealed class GameEvent {
    data class PieceMoved(val piece: PlayerPiece) : GameEvent()
    data class DiceRolled(val dice: Dice): GameEvent()
}