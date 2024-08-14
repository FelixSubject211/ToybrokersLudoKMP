package com.toybrokers.ludo.di

import com.toybrokers.ludo.DefaultNavigator
import com.toybrokers.ludo.Navigator
import org.koin.dsl.module

val navigatorModule = module {
    single<Navigator> { DefaultNavigator() }
}