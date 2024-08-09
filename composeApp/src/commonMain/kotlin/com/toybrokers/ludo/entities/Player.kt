package com.toybrokers.ludo.entities

sealed class Player {
    data object Red : Player()
    data object Blue : Player()
    data object Green : Player()
    data object Yellow : Player()

    fun next(): Player {
        return when(this) {
            is Blue -> Green
            is Green -> Yellow
            is Yellow -> Red
            is Red -> Blue
        }
    }

    fun start(): Position {
        return when(this) {
            is Red -> Position.Track(0)
            is Blue -> Position.Track(10)
            is Green -> Position.Track(20)
            is Yellow -> Position.Track(30)
        }
    }
}