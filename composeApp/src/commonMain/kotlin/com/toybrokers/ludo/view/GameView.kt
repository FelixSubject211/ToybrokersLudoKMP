package com.toybrokers.ludo.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.toybrokers.ludo.entities.GameEvent
import com.toybrokers.ludo.view.gameBoardViews.GridView
import com.toybrokers.ludo.viewmodel.GameViewModel

@Composable
fun GameView(viewModel: GameViewModel) {
    val gameState = viewModel.gameState.collectAsState()

    Column {
        Button(onClick = {
            viewModel.addEvent(GameEvent.DiceRolled(
                diceNumber = (1..6).random()
            ))
        }) {
            Text("Dice")
        }

        Text(gameState.value.error.toString())

        Text(gameState.value.currentPlayer.toString())

        Text(gameState.value.turnStatus.toString())

        Text(gameState.value.diceNumber.toString())

        GridView(gameState = gameState.value) {
            viewModel.addEvent(GameEvent.PieceMoved(it))
        }
    }
}
