package com.toybrokers.ludo.domain.entities

import kotlinx.serialization.Serializable

@Serializable
sealed class TurnStatus {

    @Serializable
    data class Dice(val remainingAttempts: Int) : TurnStatus()

    @Serializable
    data object Move : TurnStatus()

    fun remainingAttemptsOrNull(): Int? {
        return  when(this) {
            is Dice -> remainingAttempts
            Move -> null
        }
    }
}