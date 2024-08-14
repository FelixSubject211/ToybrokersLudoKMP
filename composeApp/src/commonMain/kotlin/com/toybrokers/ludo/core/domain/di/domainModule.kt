package com.toybrokers.ludo.core.domain.di

import com.toybrokers.ludo.core.domain.handlers.DefaultDiceRolledHandler
import com.toybrokers.ludo.core.domain.handlers.DefaultMoveCalculator
import com.toybrokers.ludo.core.domain.handlers.DefaultNextPlayerCalculator
import com.toybrokers.ludo.core.domain.handlers.DefaultPieceMovedHandler
import com.toybrokers.ludo.core.domain.interfaces.DiceRolledHandler
import com.toybrokers.ludo.core.domain.interfaces.MoveCalculator
import com.toybrokers.ludo.core.domain.interfaces.NextPlayerCalculator
import com.toybrokers.ludo.core.domain.interfaces.PieceMovedHandler
import org.koin.dsl.module

val domainModule = module {
    single<MoveCalculator> { DefaultMoveCalculator() }
    single<NextPlayerCalculator> { DefaultNextPlayerCalculator() }
    single<PieceMovedHandler> { DefaultPieceMovedHandler() }
    single<DiceRolledHandler> { DefaultDiceRolledHandler() }
}