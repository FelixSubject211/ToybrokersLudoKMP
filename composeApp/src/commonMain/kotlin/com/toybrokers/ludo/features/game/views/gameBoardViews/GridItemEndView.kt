package com.toybrokers.ludo.features.game.views.gameBoardViews

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.toybrokers.ludo.domain.entities.Player
import com.toybrokers.ludo.domain.entities.PlayerPiece

@Composable
fun GridItemEndView(
    gridItem: GridItem.End,
    onPlayerPieceMove: (playerPiece: PlayerPiece) -> Unit
) {
    val text = when (gridItem.playerPiece) {
        is PlayerPiece.First -> "1"
        is PlayerPiece.Second -> "2"
        is PlayerPiece.Third -> "3"
        is PlayerPiece.Fourth -> "4"
        else -> ""
    }
    val color = when(gridItem.playerPiece?.owner) {
        Player.Blue -> Color.Blue
        Player.Green -> Color.Green
        Player.Red -> Color.Red
        Player.Yellow -> Color.Yellow
        else -> {
            when(gridItem.player) {
                Player.Blue -> Color.Blue.copy(alpha = 0.5f)
                Player.Green -> Color.Green.copy(alpha = 0.5f)
                Player.Red -> Color.Red.copy(alpha = 0.5f)
                Player.Yellow -> Color.Yellow.copy(alpha = 0.5f)
                else -> Color.Black
            }
        }
    }

    Box(
        modifier = Modifier
            .size(36.dp)
            .background(color)
            .then(
                if (gridItem.playerPiece != null) {
                    Modifier.clickable { onPlayerPieceMove(gridItem.playerPiece) }
                } else {
                    Modifier
                }
            )
    ) {
        Text(text)
    }
}