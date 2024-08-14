package com.toybrokers.ludo.core.domain.interfaces

import com.toybrokers.ludo.core.domain.entities.Player

interface NextPlayerCalculator {
    fun nextPlayer(currentPlayer: Player, players: Set<Player>): Player
}