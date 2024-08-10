package com.toybrokers.ludo

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.toybrokers.ludo.application.GameEventEventSourcingManager
import com.toybrokers.ludo.presentation.views.GameView
import com.toybrokers.ludo.presentation.viewmodels.GameViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        GameView(GameViewModel(GameEventEventSourcingManager()))
    }
}