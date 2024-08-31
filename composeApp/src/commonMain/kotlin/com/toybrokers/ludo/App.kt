package com.toybrokers.ludo

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.toybrokers.ludo.di.Koin
import com.toybrokers.ludo.features.game.views.GameView
import com.toybrokers.ludo.features.manageOpenOnlineGames.ManageOpenOnlineGamesView
import com.toybrokers.ludo.features.startmenu.StartMenuView
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.core.component.get


@Composable
@Preview
fun App() {
    MaterialTheme {
        val navigator: Navigator = Koin.get()
        val currentScreen by navigator.currentScreen.collectAsState()

        when (val screen = currentScreen) {
            is Navigator.Screen.ManageOpenOnlineGames -> ManageOpenOnlineGamesView(screen.viewModel)
            is Navigator.Screen.StartMenu -> StartMenuView(screen.viewModel)
            is Navigator.Screen.GameBoard -> GameView(screen.viewModel)
            null -> {}
        }
    }
}

