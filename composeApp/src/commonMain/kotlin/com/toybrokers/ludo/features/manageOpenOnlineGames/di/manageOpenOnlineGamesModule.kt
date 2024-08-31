package com.toybrokers.ludo.features.manageOpenOnlineGames.di

import com.toybrokers.ludo.features.manageOpenOnlineGames.ManageOpenOnlineGamesViewModel
import org.koin.dsl.module

val manageOpenOnlineGamesModule = module {
    single { ManageOpenOnlineGamesViewModel(get()) }
}