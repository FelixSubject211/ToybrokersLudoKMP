package com.toybrokers.ludo.domain.di

import com.toybrokers.ludo.domain.handlers.DefaultDiceRolledHandler
import com.toybrokers.ludo.domain.handlers.DefaultMoveCalculator
import com.toybrokers.ludo.domain.handlers.DefaultNextPlayerCalculator
import com.toybrokers.ludo.domain.handlers.DefaultPieceMovedHandler
import com.toybrokers.ludo.domain.interfaces.DiceRolledHandler
import com.toybrokers.ludo.domain.interfaces.MoveCalculator
import com.toybrokers.ludo.domain.interfaces.NextPlayerCalculator
import com.toybrokers.ludo.domain.interfaces.PieceMovedHandler
import org.koin.dsl.module

val domainModule = module {
    single<MoveCalculator> { DefaultMoveCalculator() }
    single<NextPlayerCalculator> { DefaultNextPlayerCalculator() }
    single<PieceMovedHandler> {
        DefaultPieceMovedHandler(
            moveCalculator = DefaultMoveCalculator(),
            nextPlayerCalculator = DefaultNextPlayerCalculator()
        )
    }
    single<DiceRolledHandler> {
        DefaultDiceRolledHandler(
            moveCalculator = DefaultMoveCalculator(),
            nextPlayerCalculator = DefaultNextPlayerCalculator()
        )
    }
}