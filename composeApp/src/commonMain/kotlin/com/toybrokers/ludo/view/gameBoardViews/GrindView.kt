package com.toybrokers.ludo.view.gameBoardViews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.toybrokers.ludo.entities.GameState
import com.toybrokers.ludo.entities.PlayerPiece

@Composable
fun GridView(
    gameState: GameState,
    onPlayerPieceMove: (playerPiece: PlayerPiece) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(11),
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)

    ) {
        items(generateBoard(gameState).flatten()) { gridItem ->
            when(gridItem) {
                GridItem.Empty -> GridItemEmptyView()
                is GridItem.Home -> GridItemHomeView(gridItem, onPlayerPieceMove)
                is GridItem.Track -> GridItemTrackView(gridItem, onPlayerPieceMove)
            }
        }
    }
}