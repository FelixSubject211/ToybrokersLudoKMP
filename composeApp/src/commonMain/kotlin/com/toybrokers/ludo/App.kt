package com.toybrokers.ludo

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.toybrokers.ludo.application.GameEventEventSourcingManager
import com.toybrokers.ludo.application.Opponent
import com.toybrokers.ludo.domain.entities.GameState
import com.toybrokers.ludo.domain.entities.Player
import com.toybrokers.ludo.presentation.views.GameView
import com.toybrokers.ludo.presentation.viewmodels.GameViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        val gameState = GameState.initialState(
            Player.Green, Player.Yellow, Player.Red, Player.Blue
        )
        val gameEventManager = GameEventEventSourcingManager(gameState)
        val viewModel = GameViewModel(gameEventManager)

        GameView(viewModel)

        Opponent(Player.Yellow, gameEventManager).start()
        Opponent(Player.Green, gameEventManager).start()
        Opponent(Player.Blue, gameEventManager).start()
    }
}