package com.toybrokers.ludo.view.gameBoardViews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun GridItemEmptyView() {
    Box(
        modifier = Modifier
            .size(36.dp)
            .background(Color.Transparent)
    )
}