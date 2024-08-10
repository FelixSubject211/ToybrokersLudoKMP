package com.toybrokers.ludo.domain.entities

sealed class Position {
    data class Home(val playerPiece: PlayerPiece) : Position()
    data class Track(val value: Int, val maxIndex: Int = 39): Position() {
        init {
            require(value in 0..maxIndex) {
                "Track index must be between 0 and $maxIndex"
            }
        }

        fun increment(by: Int, player: Player): Position? {
            val newValue = (value + by)

            if(player.lastIndexBeforeEnd() in value..<newValue) {
                val offset = newValue - player.lastIndexBeforeEnd()
                return if(offset <= 3) {
                    End(player, offset - 1)
                } else {
                    null
                }
            } else {
                return Track(newValue % (maxIndex + 1), maxIndex)
            }
        }
    }
    data class End(val player: Player, val value: Int, val maxIndex: Int = 3): Position() {
        init {
            require(value in 0..maxIndex) {
                "End index must be between 0 and $maxIndex"
            }
        }

        fun increment(by: Int): Position? {
            val newValue = (value + by)
            return if (newValue <= maxIndex) {
                End(player, newValue)
            } else {
                null
            }
        }
    }
}