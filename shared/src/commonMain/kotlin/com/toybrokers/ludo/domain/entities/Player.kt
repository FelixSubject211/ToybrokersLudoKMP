package com.toybrokers.ludo.domain.entities

import com.toybrokers.ludo.BuildKonfig
import kotlinx.serialization.Serializable

@Serializable
sealed class Player {
    @Serializable
    data object Red : Player()
    @Serializable
    data object Blue : Player()
    @Serializable
    data object Green : Player()
    @Serializable
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