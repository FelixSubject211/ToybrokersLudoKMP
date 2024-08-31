package com.toybrokers.ludo.di

import com.toybrokers.ludo.Navigator
import com.toybrokers.ludo.domain.di.domainModule
import com.toybrokers.ludo.features.game.di.gameModule
import com.toybrokers.ludo.features.manageOpenOnlineGames.di.manageOpenOnlineGamesModule
import com.toybrokers.ludo.features.startmenu.di.startmenuModule
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin

object Koin: KoinComponent {

    fun init() {
        startKoin {
            modules(
                domainModule,
                navigatorModule,
                gameModule,
                startmenuModule,
                manageOpenOnlineGamesModule
            )
        }

        val navigator: Navigator = get()
        navigator.navigateTo(Navigator.Screen.ManageOpenOnlineGames(get()))
    }
}