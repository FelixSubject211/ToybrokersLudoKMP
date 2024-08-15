package com.toybrokers.ludo.features.startmenu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toybrokers.ludo.domain.entities.Player

@Composable
fun StartMenuView(viewModel: StartMenuViewModel) {
    val playersState by viewModel.playersState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Ludo Game Menu",
                        fontSize = 24.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val players = playersState.keys.toList()
                    for (i in players.indices step 2) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            PlayerSelectionRow(
                                player = players[i],
                                currentSelection = playersState[players[i]]!!,
                                onSelectionChanged = { newState ->
                                    viewModel.editPlayerState(players[i], newState)
                                }
                            )
                            if (i + 1 < players.size) {
                                PlayerSelectionRow(
                                    player = players[i + 1],
                                    currentSelection = playersState[players[i + 1]]!!,
                                    onSelectionChanged = { newState ->
                                        viewModel.editPlayerState(players[i + 1], newState)
                                    }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                Button(
                    onClick = viewModel::startGame,
                    enabled = viewModel.playerStateIsValid(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Start Game")
                }
            }
        }
    )
}

@Composable
fun PlayerSelectionRow(
    player: Player,
    currentSelection: StartMenuViewModel.PlayerState,
    onSelectionChanged: (StartMenuViewModel.PlayerState) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
    ) {
        Text(player.toString(), modifier = Modifier.padding(bottom = 8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = currentSelection is StartMenuViewModel.PlayerState.Inactive,
                onClick = { onSelectionChanged(StartMenuViewModel.PlayerState.Inactive) },
                colors = RadioButtonDefaults.colors(selectedColor = colors.primary)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Inaktiv")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = currentSelection is StartMenuViewModel.PlayerState.ControlledByHuman,
                onClick = { onSelectionChanged(StartMenuViewModel.PlayerState.ControlledByHuman) },
                colors = RadioButtonDefaults.colors(selectedColor = colors.primary)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Human")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = currentSelection is StartMenuViewModel.PlayerState.ControlledByComputer,
                onClick = { onSelectionChanged(StartMenuViewModel.PlayerState.ControlledByComputer) },
                colors = RadioButtonDefaults.colors(selectedColor = colors.primary)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Computer")
        }
    }
}