package com.toybrokers.ludo

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.toybrokers.ludo.features.game.views.GameView
import com.toybrokers.ludo.features.manageOpenOnlineGames.ManageOpenOnlineGamesView
import com.toybrokers.ludo.features.manageOpenOnlineGames.ManageOpenOnlineGamesViewModel
import com.toybrokers.ludo.features.startmenu.StartMenuView
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val navigator:Navigator = DefaultNavigator()
    navigator.navigateTo(Navigator.Screen.ManageOpenOnlineGames(
        ManageOpenOnlineGamesViewModel(navigator)
    ))

    MaterialTheme {
        val currentScreen by navigator.currentScreen.collectAsState()

        when (val screen = currentScreen) {
            is Navigator.Screen.ManageOpenOnlineGames -> ManageOpenOnlineGamesView(screen.viewModel)
            is Navigator.Screen.StartMenu -> StartMenuView(screen.viewModel)
            is Navigator.Screen.GameBoard -> GameView(screen.viewModel)
            null -> {}
        }
    }
}

