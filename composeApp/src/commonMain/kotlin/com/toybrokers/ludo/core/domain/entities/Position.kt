package com.toybrokers.ludo.core.domain.entities

import com.toybrokers.ludo.BuildKonfig

sealed class Position {
    data class Home(val playerPiece: PlayerPiece) : Position()
    data class Track(val value: Int): Position() {
        init {
            require(value in 0..BuildKonfig.trackMaxNumber) {
                "Track index must be between 0 and $BuildKonfig.trackMaxNumber"
            }
        }

        fun increment(by: Int, player: Player): Position? {
            val newValue = (value + by)

            if(player.lastIndexBeforeEnd() in value..<newValue) {
                val offset = newValue - player.lastIndexBeforeEnd()
                return if(offset <= BuildKonfig.endMaxNumber) {
                    End(player, offset - 1)
                } else {
                    null
                }
            } else {
                return Track(newValue % (BuildKonfig.trackMaxNumber + 1))
            }
        }
    }
    data class End(val player: Player, val value: Int): Position() {
        init {
            require(value in 0..BuildKonfig.endMaxNumber) {
                "End index must be between 0 and $BuildKonfig.endMaxNumber"
            }
        }

        fun increment(by: Int): Position? {
            val newValue = (value + by)
            return if (newValue <= BuildKonfig.endMaxNumber) {
                End(player, newValue)
            } else {
                null
            }
        }
    }
}