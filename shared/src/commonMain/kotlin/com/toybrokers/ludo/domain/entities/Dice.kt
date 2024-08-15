package com.toybrokers.ludo.domain.entities

import com.toybrokers.ludo.BuildKonfig


data class Dice(
    val diceNumber: Int,
    val uuidForUIUpdates: UUID = UUID.randomUUID()
) {
    init {
        require(diceNumber in 1..BuildKonfig.maxDiceNumber) {
            "DiceNumber index must be between 1 and $BuildKonfig.maxDiceNumber"
        }
    }

    fun rolled(): Dice {
        return this.copy(
            diceNumber = (1..BuildKonfig.maxDiceNumber).random(),
            uuidForUIUpdates = UUID.randomUUID()
        )
    }

    val diceNumberIsMax: Boolean = diceNumber == BuildKonfig.maxDiceNumber

    /**
     * Note: The UUID field is generally used for UI purposes
     * to uniquely identify each Dice instance, especially when
     * multiple dice need to be tracked or displayed. It is not
     * used in equality checks to allow different instances with
     * the same diceNumber and maxNumber to be considered equal.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Dice) return false

        if (diceNumber != other.diceNumber) return false

        return true
    }

    override fun hashCode(): Int {
        var result = diceNumber
        result = 31 * result + BuildKonfig.maxDiceNumber
        return result
    }
}
