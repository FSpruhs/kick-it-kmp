package com.spruhs.screens.match

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.AssistChip
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.spruhs.match.application.PlayerStatus
import com.spruhs.match.presentation.UpcomingMatchDetailsEffect
import com.spruhs.match.presentation.UpcomingMatchDetailsIntent
import com.spruhs.match.presentation.UpcomingMatchDetailsUIState
import com.spruhs.match.presentation.UpcomingMatchDetailsViewModel
import com.spruhs.permission.PermissionManager
import com.spruhs.screens.common.CancelButton
import com.spruhs.screens.common.ConfirmAlertDialog
import com.spruhs.screens.common.PlayerMatchStatusIcon
import com.spruhs.screens.common.RoleBasedVisibility
import com.spruhs.screens.common.SubmitButton
import com.spruhs.screens.user.RegistrationDisplay
import com.spruhs.ui.theme.CustomColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun UpcomingMatchDetailsScreen(
    matchId: String,
    onMatchCancelled: () -> Unit,
    upcomingMatchDetailsViewModel: UpcomingMatchDetailsViewModel = koinViewModel()
) {
    val uiState by upcomingMatchDetailsViewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        upcomingMatchDetailsViewModel.effects.collect { effect ->
            when (effect) {
                UpcomingMatchDetailsEffect.MatchCancelled -> {
                    Toast
                        .makeText(
                            context,
                            "Match cancelled!",
                            Toast.LENGTH_SHORT
                        ).show()
                    onMatchCancelled()
                }
            }

            if (uiState.error != null) {
                Toast
                    .makeText(
                        context,
                        uiState.error,
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        content = { paddingValues ->
            UpcomingMatchDetailContent(
                modifier = Modifier.padding(paddingValues),
                uiState = uiState,
                onIntent = upcomingMatchDetailsViewModel::processIntent
            )
        }
    )
}

@Composable
fun UpcomingMatchDetailContent(
    modifier: Modifier = Modifier,
    uiState: UpcomingMatchDetailsUIState,
    onIntent: (UpcomingMatchDetailsIntent) -> Unit
) {
    var showDeregisterDialog by remember { mutableStateOf(false) }
    var menuExpanded by remember { mutableStateOf(false) }
    var showCancelDialog by remember { mutableStateOf(false) }

    Column(
        modifier =
        modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MatchShortInfo(
            uiState = uiState
        )

        HorizontalDivider(
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )

        RegistrationSelector(
            isRegistered = uiState.selectedRegistration,
            onSelectionChange = {
                onIntent(UpcomingMatchDetailsIntent.SelectRegistration(it ?: false))
            },
            isLoading = uiState.isLoading,
            onButtonClick = { registration ->
                if (uiState.startRegistration == true && !registration) {
                    showDeregisterDialog = true
                } else {
                    onIntent(UpcomingMatchDetailsIntent.RegisterPlayer(registration))
                }
            }
        )

        HorizontalDivider(modifier = Modifier.padding(8.dp))

        PlayerSection(
            title = "Cadre",
            icon = Icons.Rounded.Check,
            players = uiState.cadre,
            groupNameList = uiState.groupNameList,
            color = CustomColor.Green,
            menuText = "Remove from cadre",
            onMenuClick = { id -> onIntent(UpcomingMatchDetailsIntent.RemoveFromCadre(id)) },
            readOnly =
            !PermissionManager.hasPermission(
                uiState.userRole,
                "upcomingMatchDetailScreen:removePlayerFromCadre"
            )
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )

        PlayerSection(
            title = "Waitingbench",
            icon = Icons.Default.HourglassEmpty,
            players = uiState.waitingBench,
            groupNameList = uiState.groupNameList,
            color = CustomColor.Gray,
            menuText = "Add to cadre",
            onMenuClick = { id -> onIntent(UpcomingMatchDetailsIntent.AddToCadre(id)) },
            readOnly =
            !PermissionManager.hasPermission(
                uiState.userRole,
                "upcomingMatchDetailScreen:addPlayerFromCadre"
            )
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )

        PlayerSection(
            title = "Deregistered",
            icon = Icons.Default.Close,
            players = uiState.deregistered,
            groupNameList = uiState.groupNameList,
            color = CustomColor.Red,
            readOnly = true
        )

        RoleBasedVisibility(
            uiState.userRole,
            "upcomingMatchDetailScreen:cancelMatchButton"
        ) {
            CancelButton(
                menuExpanded = menuExpanded,
                setShowDialog = { showCancelDialog = it },
                setMenuExpanded = { menuExpanded = it },
                text = "Cancel Match"
            )
        }

        if (showCancelDialog) {
            ConfirmAlertDialog(
                text = "Are you sure you want to cancel this match?",
                onDismiss = { showCancelDialog = false },
                onConfirm = {
                    showCancelDialog = false
                    onIntent(UpcomingMatchDetailsIntent.CancelMatch)
                }
            )
        }

        if (showDeregisterDialog) {
            ConfirmAlertDialog(
                text = "When you deregister you loose your position in the queue.",
                onDismiss = { showDeregisterDialog = false },
                onConfirm = {
                    showDeregisterDialog = false
                    onIntent(UpcomingMatchDetailsIntent.RegisterPlayer(false))
                }
            )
        }
    }
}

@Composable
fun PlayerSection(
    title: String,
    icon: ImageVector,
    players: List<String>,
    groupNameList: Map<String, String>,
    menuText: String? = null,
    onMenuClick: (String) -> Unit = {},
    color: Color,
    readOnly: Boolean = false
) {
    Row(
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = players.size.toString(),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }

    PlayerChipArea(
        players = players,
        groupNameList = groupNameList,
        color = color,
        menuText = menuText,
        onClick = onMenuClick,
        readOnly = readOnly
    )
}

@Composable
fun MatchShortInfo(
    modifier: Modifier = Modifier,
    uiState: UpcomingMatchDetailsUIState
) {
    Row(
        modifier =
        modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
            Modifier
                .padding(end = 16.dp)
                .width(72.dp)
                .weight(1F)
        ) {
            PlayerMatchStatusIcon(
                status = uiState.userPosition,
                size = 48
            )
            Text(
                text = "Your Position",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text =
                when (uiState.userPosition) {
                    PlayerStatus.CADRE -> "Cadre"
                    PlayerStatus.WAITING_BENCH -> "Waiting on Bench"
                    PlayerStatus.DEREGISTERED -> "Deregistered"
                    null -> "No Selection"
                },
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }

        Column(
            modifier = Modifier.weight(1F),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column {
                Text(
                    text = "Date",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = uiState.start?.toString() ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            Column {
                Text(
                    text = "Playground",
                    style = MaterialTheme.typography.titleMedium
                )
                if (uiState.playground.isNullOrBlank()) {
                    "-"
                } else {
                    uiState.playground?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
        Column(
            modifier = Modifier.weight(1F),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RegistrationDisplay(
                size = 72,
                actual = uiState.actualPlayersCount,
                from = uiState.maxPlayers
            )
        }
    }
}

@Composable
fun RegistrationSelector(
    isRegistered: Boolean?,
    onSelectionChange: (Boolean?) -> Unit,
    isLoading: Boolean,
    onButtonClick: (Boolean) -> Unit
) {
    Row(
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier =
            Modifier
                .border(
                    width = 1.dp,
                    color =
                    if (isRegistered == true) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color.Transparent
                    },
                    shape = MaterialTheme.shapes.small
                )
                .weight(1f)
                .height(64.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isRegistered == true) Color(0xFF81C784) else Color(0xFFB2DFDB))
                .clickable { onSelectionChange(true) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Register",
                color = Color.Black,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Box(
            modifier =
            Modifier
                .border(
                    width = 1.dp,
                    color =
                    if (isRegistered == false) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color.Transparent
                    },
                    shape = MaterialTheme.shapes.small
                )
                .weight(1f)
                .height(64.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isRegistered == false) Color(0xFFE57373) else Color(0xFFFFCDD2))
                .clickable { onSelectionChange(false) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Deregister",
                color = Color.Black,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        SubmitButton(
            enabled = isRegistered != null && !isLoading,
            isLoading = isLoading
        ) {
            onButtonClick(isRegistered ?: false)
        }
    }
}

@Composable
fun PlayerChipArea(
    color: Color,
    players: List<String>,
    groupNameList: Map<String, String>,
    modifier: Modifier = Modifier,
    menuText: String? = null,
    readOnly: Boolean = false,
    onClick: (String) -> Unit = {}
) {
    Box(
        modifier =
        modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(color, RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        FlowRow(
            modifier =
            Modifier
                .fillMaxWidth()
                .heightIn(min = (32.dp + 8.dp) * 3),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            players.forEach { player ->
                if (menuText != null && !readOnly) {
                    var expanded by remember { mutableStateOf(false) }

                    Box {
                        AssistChip(
                            onClick = { expanded = true },
                            label = { Text(groupNameList[player] ?: "Unknown") }
                        )

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(menuText) },
                                onClick = {
                                    onClick(player)
                                    expanded = false
                                }
                            )
                        }
                    }
                } else {
                    AssistChip(
                        onClick = { },
                        label = { Text(groupNameList[player] ?: "Unknown") }
                    )
                }
            }
        }
    }
}