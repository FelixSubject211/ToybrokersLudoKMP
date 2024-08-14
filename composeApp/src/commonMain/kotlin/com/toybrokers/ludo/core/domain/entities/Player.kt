package com.toybrokers.ludo.core.domain.entities

import com.toybrokers.ludo.BuildKonfig

sealed class Player {
    data object Red : Player()
    data object Blue : Player()
    data object Green : Player()
    data object Yellow : Player()

    fun start(): Position {
        return when(this) {
            is Red -> Position.Track(BuildKonfig.redStart)
            is Blue -> Position.Track(BuildKonfig.blueStart)
            is Green -> Position.Track(BuildKonfig.greenStart)
            is Yellow -> Position.Track(BuildKonfig.yellowStart)
        }
    }

    fun lastIndexBeforeEnd(): Int {
        return when(this) {
            Blue -> BuildKonfig.blueLastIndexBeforeEnd
            Green -> BuildKonfig.greenLastIndexBeforeEnd
            Yellow -> BuildKonfig.yellowLastIndexBeforeEnd
            Red -> BuildKonfig.redLastIndexBeforeEnd
        }
    }
}