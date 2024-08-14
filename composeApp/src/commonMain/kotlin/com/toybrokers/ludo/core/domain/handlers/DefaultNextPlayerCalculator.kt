package com.toybrokers.ludo.core.domain.handlers

import com.toybrokers.ludo.core.domain.entities.Player
import com.toybrokers.ludo.core.domain.entities.Player.Blue
import com.toybrokers.ludo.core.domain.entities.Player.Green
import com.toybrokers.ludo.core.domain.entities.Player.Red
import com.toybrokers.ludo.core.domain.entities.Player.Yellow
import com.toybrokers.ludo.core.domain.interfaces.NextPlayerCalculator

class DefaultNextPlayerCalculator: NextPlayerCalculator {
    override fun nextPlayer(currentPlayer: Player, players: Set<Player>): Player {
        return generateSequence(currentPlayer) { next(it) }
            .drop(1)
            .first { it in players }
    }

    private fun next(player: Player): Player {
        return when(player) {
            is Blue -> Green
            is Green -> Yellow
            is Yellow -> Red
            is Red -> Blue
        }
    }
}