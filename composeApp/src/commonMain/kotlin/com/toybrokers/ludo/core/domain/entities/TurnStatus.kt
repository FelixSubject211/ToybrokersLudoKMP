package com.toybrokers.ludo.core.domain.entities

sealed class TurnStatus {

    data class Dice(val remainingAttempts: Int) : TurnStatus()

    data object Move : TurnStatus()

    fun remainingAttemptsOrNull(): Int? {
        return  when(this) {
            is Dice -> remainingAttempts
            Move -> null
        }
    }
}