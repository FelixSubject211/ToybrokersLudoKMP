package com.toybrokers.ludo.features.manageOpenOnlineGames

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Scaffold
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toybrokers.ludo.domain.entities.Player
import com.toybrokers.ludo.network.manageOpenOnlineGamesService.ManageOpenOnlineGamesServiceResponse


@Composable
fun ManageOpenOnlineGamesView(viewModel: ManageOpenOnlineGamesViewModel) {
    val games = viewModel.games.collectAsState()
    val error = viewModel.error.collectAsState()
    val joinGameError = viewModel.joinGameError.collectAsState()
    val showPlayerSelector = viewModel.showPlayerSelector.collectAsState()
    val selectedGameId = viewModel.selectedGameId.collectAsState()

    joinGameError.value?.let {
        AlertDialog(
            onDismissRequest = {  },
            title = {
                Text(it)
            },
            confirmButton = {
                TextButton(onClick = viewModel::closeJoinGameError) {
                    Text("ok")
                }
            },
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Online",
                        fontSize = 24.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.setSelectedGameId(null)
                        viewModel.setShowPlayerSelector(true)
                    }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Open New Game")
                    }
                }
            )
        },
    ) {
        if (error.value == null) {
            if (showPlayerSelector.value) {
                PlayerSelectionDialog(
                    onPlayerSelected = { player ->
                        viewModel.setSelectedPlayer(player)
                        if (selectedGameId.value != null) {
                            viewModel.joinGame()
                        } else {
                            viewModel.openNewGame()
                        }
                    },
                    onDismiss = { viewModel.setShowPlayerSelector(false) }
                )
            }

            LazyColumn(
                modifier = Modifier.padding(16.dp)
            ) {
                items(games.value.games) { game ->
                    GameItem(
                        game = game,
                        onClick = {
                            viewModel.setSelectedGameId(game.gameId)
                            viewModel.setShowPlayerSelector(true)
                        }
                    )
                }
            }
        } else {
            Column {
                Text(error.value?.message ?: "error")
                Button(onClick = viewModel::initService) {
                    Text("retry")
                }
            }
        }
    }
}

@Composable
private fun GameItem(game: ManageOpenOnlineGamesServiceResponse.PendingOnlineGames.Game, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = game.gameId,
                style = typography.body1.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Divider(color = Color.Gray, thickness = 1.dp)
            Text(
                text = game.players.joinToString(separator = ", ") { it.toString() },
                style = typography.body2,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun PlayerSelectionDialog(onPlayerSelected: (Player) -> Unit, onDismiss: () -> Unit) {
    var selectedPlayer by remember { mutableStateOf<Player>(Player.Red) }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Select Player") },
        text = {
            Column {
                Button(onClick = { expanded = true }) {
                    Text(selectedPlayer.toString())
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    listOf(Player.Red, Player.Blue, Player.Yellow, Player.Green).forEach { player ->
                        DropdownMenuItem(onClick = {
                            selectedPlayer = player
                            expanded = false
                        }) {
                            Text(player.toString())
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onPlayerSelected(selectedPlayer) }) {
                Text("OK")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
