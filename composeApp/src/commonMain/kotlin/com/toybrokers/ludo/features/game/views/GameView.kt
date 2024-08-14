package com.toybrokers.ludo.features.game.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toybrokers.ludo.core.domain.events.GameEvent
import com.toybrokers.ludo.features.game.GameViewModel
import com.toybrokers.ludo.features.game.views.gameBoardViews.GridView

@Composable
fun GameView(viewModel: GameViewModel) {
    val gameState = viewModel.gameState.collectAsState()
    val menuExpanded = viewModel.menuExpanded.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Ludo Game",
                        fontSize = 24.sp
                    )
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleMenu() }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }
                    DropdownMenu(
                        expanded = menuExpanded.value,
                        onDismissRequest = { viewModel.onDismissMenu() }
                    ) {
                        DropdownMenuItem(onClick = viewModel::undo) {
                            Text("Undo")
                        }

                        DropdownMenuItem(onClick = viewModel::onEndGame) {
                            Text("Leave Game")
                        }
                    }
                }
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
                        dice = gameState.value.dice,
                        onTap = {
                            viewModel.addEvent(GameEvent.DiceRolled(it))
                        },
                        modifier = Modifier.padding(8.dp)
                    )
                }

                CustomSnackbar(gameState)
            }
        }
    )
}
