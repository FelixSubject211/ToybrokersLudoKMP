package com.toybrokers.ludo.features.game.views.gameBoardViews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.toybrokers.ludo.domain.entities.GameState
import com.toybrokers.ludo.domain.entities.PlayerPiece

@Composable
fun GridView(
    gameState: GameState,
    onPlayerPieceMove: (playerPiece: PlayerPiece) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(11),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .padding(8.dp)

    ) {
        items(generateBoard(gameState).flatten()) { gridItem ->
            when(gridItem) {
                GridItem.Empty -> GridItemEmptyView()
                is GridItem.Home -> GridItemHomeView(gridItem, onPlayerPieceMove)
                is GridItem.Track -> GridItemTrackView(gridItem, onPlayerPieceMove)
                is GridItem.End -> GridItemEndView(gridItem, onPlayerPieceMove)
            }
        }
    }
}