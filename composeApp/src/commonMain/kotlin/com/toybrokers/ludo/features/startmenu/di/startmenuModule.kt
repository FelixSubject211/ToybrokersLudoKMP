package com.toybrokers.ludo.features.startmenu.di

import com.toybrokers.ludo.features.startmenu.StartMenuViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val startmenuModule = module {
    singleOf(::StartMenuViewModel)
}