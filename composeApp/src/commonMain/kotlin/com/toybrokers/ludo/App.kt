package com.toybrokers.ludo

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.toybrokers.ludo.controllers.GameEventEventSourcingManager
import com.toybrokers.ludo.view.GameView
import com.toybrokers.ludo.viewmodel.GameViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        GameView(GameViewModel(GameEventEventSourcingManager()))
    }
}