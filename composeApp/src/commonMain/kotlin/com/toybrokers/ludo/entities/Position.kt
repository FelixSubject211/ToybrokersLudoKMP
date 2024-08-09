package com.toybrokers.ludo.entities

sealed class Position {
    data class Home(val playerPiece: PlayerPiece) : Position()
    data class Track(val value: Int, val maxIndex: Int = 39): Position() {
        init {
            require(value in 0..maxIndex) {
                "Track index must be between 0 and $maxIndex"
            }
        }

        fun increment(by: Int): Track {
            val newValue = (value + by) % (maxIndex + 1)
            return Track(newValue, maxIndex)
        }
    }
}