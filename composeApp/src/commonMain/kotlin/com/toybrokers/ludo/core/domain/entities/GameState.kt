package com.toybrokers.ludo.core.domain.entities


data class GameState(
    val positions: Map<Position, PlayerPiece>,
    val currentPlayer: Player,
    val players: Set<Player>,
    val turnStatus: TurnStatus,
    val dice: Dice,
    val error: GameError?
) {
    companion object {
        fun initialState(players: Set<Player>): GameState {
            val positions: Map<Position, PlayerPiece> = players
                .flatMap { player ->
                    PlayerPiece.all(player).map { piece -> piece.home() to piece }
                }
                .toMap()

            return GameState(
                positions = positions,
                currentPlayer = players.random(),
                players = players,
                turnStatus = TurnStatus.Dice(remainingAttempts = 2),
                dice = Dice(diceNumber = 1),
                error = null
            )
        }
    }
}