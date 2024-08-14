package com.toybrokers.ludo.features.game.di

import com.toybrokers.ludo.features.game.GameViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val gameModule = module {
    singleOf(::GameViewModel)
}