package com.toybrokers.ludo.core.domain.entities

sealed class PlayerPiece(val owner: Player) {
    data class First(val pieceOwner: Player) : PlayerPiece(pieceOwner)
    data class Second(val pieceOwner: Player) : PlayerPiece(pieceOwner)
    data class Third(val pieceOwner: Player) : PlayerPiece(pieceOwner)
    data class Fourth(val pieceOwner: Player) : PlayerPiece(pieceOwner)

    fun home(): Position.Home {
        return when(this) {
            is First -> Position.Home(this)
            is Fourth -> Position.Home(this)
            is Second -> Position.Home(this)
            is Third -> Position.Home(this)
        }
    }

    companion object {
        fun all(owner: Player): List<PlayerPiece> {
            return listOf(
                First(owner),
                Second(owner),
                Third(owner),
                Fourth(owner)
            )
        }
    }
}