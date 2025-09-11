package com.spruhs.screens.group

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.spruhs.group.GroupEffect
import com.spruhs.group.GroupIntent
import com.spruhs.group.GroupUiState
import com.spruhs.group.GroupViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun GroupScreen(
    setBackIcon: (Boolean) -> Unit,
    onInvitePlayerClick: () -> Unit,
    onPlayerClick: (String) -> Unit,
    onLeaveGroup: () -> Unit,
    groupViewModel: GroupViewModel = koinViewModel(),
) {

    val groupUIState by groupViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        setBackIcon(false)
        groupViewModel.effects.collect { effect ->
            when (effect) {
                is GroupEffect.LeavedGroup -> {
                    onLeaveGroup()
                }
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        content = { paddingValues ->
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(paddingValues)
                        .padding(6.dp)
            ) {
                GroupPlayersContent(
                    groupUIState = groupUIState,
                    onIntent = groupViewModel::processIntent
                )
            }
        },
        floatingActionButton = {
            RoleBasedVisibility(
                selectedGroup?.role,
                "groupScreen:addPlayerButton"
            ) {
                FloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = onInvitePlayerClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    )
}

@Composable
fun GroupPlayersContent(
    groupUIState: GroupUiState,
    onIntent: (GroupIntent) -> Unit,
) {
    var menuExpanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    DataContent(
        dataUIState = groupUIState
    ) {
        val players = groupUIState.data?.players ?: emptyList()
        if (players.isNotEmpty()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(players) { player ->
                    PlayerItem(
                        player,
                        groupNames,
                        onPlayerClick
                    )
                }

                item {
                    CancelButton(
                        menuExpanded = menuExpanded,
                        setShowDialog = { showDialog = it },
                        setMenuExpanded = { menuExpanded = it },
                        text = "Leave Group"
                    )
                }
            }
        } else {
            Text(text = "No players found")
        }
    }

    if (showDialog) {
        ConfirmAlertDialog(
            text = "Are you sure you want to leave the group?",
            onDismiss = { showDialog = false },
            onConfirm = {
                showDialog = false
                onLeaveGroup()
            }
        )
    }
}

@Composable
fun PlayerItem(
    player: PlayerDetails,
    groupNamesUIState: Map<String, String>,
    onClick: (String) -> Unit
) {
    Card(
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .graphicsLayer(alpha = if (player.status != UserStatus.ACTIVE) 0.5f else 1f)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = LocalIndication.current
                ) {
                    onClick(player.id)
                }
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                UserImage(
                    imageUrl = player.avatarUrl,
                    size = 50
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = groupNamesUIState[player.id] ?: "Unknown Name",
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}