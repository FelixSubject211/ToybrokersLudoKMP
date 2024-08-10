package com.toybrokers.ludo.presentation.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toybrokers.ludo.domain.entities.GameEvent
import com.toybrokers.ludo.domain.entities.TurnStatus
import com.toybrokers.ludo.presentation.views.gameBoardViews.GridView
import com.toybrokers.ludo.presentation.viewmodels.GameViewModel

@Composable
fun GameView(viewModel: GameViewModel) {
    val gameState = viewModel.gameState.collectAsState()

    Scaffold(
        topBar = {
            Text(
                text = "Ludo Game",
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues)
            ) {
                GridView(gameState = gameState.value) {
                    viewModel.addEvent(GameEvent.PieceMoved(it))
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Current Player: ${gameState.value.currentPlayer}",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )

                    AnimatedDice(
                        diceNumber = gameState.value.diceNumber,
                        onRollCompleted = {
                            viewModel.addEvent(
                                GameEvent.DiceRolled(
                                diceNumber = it
                            ))
                        },
                        canRoll = gameState.value.turnStatus is TurnStatus.Dice,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                CustomSnackbar(gameState)
            }
        }
    )
}
