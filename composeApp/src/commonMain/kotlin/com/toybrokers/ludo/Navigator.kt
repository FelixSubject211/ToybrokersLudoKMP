package com.toybrokers.ludo

import com.toybrokers.ludo.features.game.GameViewModel
import com.toybrokers.ludo.features.startmenu.StartMenuViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface Navigator {
    val currentScreen: StateFlow<Screen>

    fun navigateTo(screen: Screen)
    fun goBack()

    sealed class Screen {
        data class StartMenu(val viewModel: StartMenuViewModel) : Screen()
        data class GameBoard(val viewModel: GameViewModel) : Screen()
    }

    companion object {
        val defaultNavigator = DefaultNavigator(
            initialScreen = Screen.StartMenu(StartMenuViewModel())
        )
    }
}

class DefaultNavigator(initialScreen: Navigator.Screen): Navigator {
    private val _currentScreen = MutableStateFlow(initialScreen)
    override val currentScreen: StateFlow<Navigator.Screen> get() = _currentScreen

    private val screenStack = mutableListOf<Navigator.Screen>()

    override fun navigateTo(screen: Navigator.Screen) {
        screenStack.add(_currentScreen.value)
        _currentScreen.value = screen
    }

    override fun goBack() {
        if (screenStack.isNotEmpty()) {
            _currentScreen.value = screenStack.removeAt(screenStack.size - 1)
        }
    }
}