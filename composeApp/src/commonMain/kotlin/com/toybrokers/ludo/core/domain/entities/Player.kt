package com.toybrokers.ludo.core.domain.entities

sealed class Player {
    data object Red : Player()
    data object Blue : Player()
    data object Green : Player()
    data object Yellow : Player()

    fun start(): Position {
        return when(this) {
            is Red -> Position.Track(0)
            is Blue -> Position.Track(10)
            is Green -> Position.Track(20)
            is Yellow -> Position.Track(30)
        }
    }

    fun lastIndexBeforeEnd(): Int {
        return when(this) {
            Blue -> 9
            Green -> 19
            Yellow -> 29
            Red -> 39
        }
    }
}