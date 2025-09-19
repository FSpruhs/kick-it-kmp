package com.spruhs.screens.group

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Handshake
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.spruhs.group.application.PlayerDetails
import com.spruhs.group.presentation.PlayerDetailsEffect
import com.spruhs.group.presentation.PlayerDetailsIntent
import com.spruhs.group.presentation.PlayerDetailsUIState
import com.spruhs.group.presentation.PlayerDetailsViewModel
import com.spruhs.match.application.Match
import com.spruhs.match.application.PlayerMatchResult
import com.spruhs.match.application.PlayerStatus
import com.spruhs.permission.PermissionManager
import com.spruhs.screens.common.ConfirmAlertDialog
import com.spruhs.screens.common.SubmitButton
import com.spruhs.screens.common.UserImage
import com.spruhs.statistics.application.PlayerStats
import com.spruhs.ui.theme.CustomColor
import com.spruhs.user.application.LabeledEnum
import com.spruhs.user.application.UserRole
import com.spruhs.user.application.UserStatus
import org.koin.androidx.compose.koinViewModel
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun PlayerDetailsScreen(
    playerId: String,
    onLastMatchClick: (String) -> Unit,
    onPlayerRemoved: () -> Unit,
    playerDetailsViewModel: PlayerDetailsViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val playerDetailsUIState by playerDetailsViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        playerDetailsViewModel.effects.collect { effect ->
            when (effect) {
                PlayerDetailsEffect.PlayerRemoved -> {
                    Toast
                        .makeText(
                            context,
                            "Player removed!",
                            Toast.LENGTH_SHORT
                        ).show()
                    onPlayerRemoved()
                }

                PlayerDetailsEffect.PlayerUpdated ->  {
                    Toast
                        .makeText(
                            context,
                            "Player updated!",
                            Toast.LENGTH_SHORT
                        ).show()
                }
            }

            if (playerDetailsUIState.error != null) {
                Toast
                    .makeText(
                        context,
                        playerDetailsUIState.error,
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        content = { paddingValues ->
            GroupDetailsContent(
                modifier = Modifier.padding(paddingValues),
                onIntent = playerDetailsViewModel::processIntent,
                uiState = playerDetailsUIState,
            )
        }
    )
}

@Composable
fun GroupDetailsContent(
    modifier: Modifier = Modifier,
    onIntent: (PlayerDetailsIntent) -> Unit,
    uiState: PlayerDetailsUIState,
) {
    when {
        uiState.isLoading -> {
            CircularProgressIndicator()
        }

        uiState.error != null -> {
            Text(text = uiState.error ?: "")
        }

        else -> {
            PlayerDetailsContent(
                modifier = modifier,
                onIntent = onIntent,
                uiState = uiState,
            )
        }
    }
}

@Composable
fun PlayerDetailsContent(
    modifier: Modifier = Modifier,
    onIntent: (PlayerDetailsIntent) -> Unit,
    uiState: PlayerDetailsUIState
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
    ) {
        PlayerShortInfo(uiState.playerDetails, uiState.groupNames)
        PlayerStatsCard(uiState.playerStats)

        HorizontalDivider(
            modifier = Modifier.padding(14.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )

        LastMatches(
            onIntent = onIntent,
            lastMatches = uiState.lastMatches,
            playerId = uiState.playerDetails?.id ?: ""
        )

        HorizontalDivider(
            modifier = Modifier.padding(14.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )

        PlayerProperties(
            playerDetails = uiState.playerDetails,
            uiState = uiState,
            onIntent = onIntent,
            readOnly =
                !PermissionManager.hasPermission(
                    uiState.selectedGroup?.role,
                    "playerDetailsScreen:playerPropertiesReadOnly"
                )
        )
    }
}

@Composable
fun PlayerProperties(
    uiState: PlayerDetailsUIState,
    playerDetails: PlayerDetails?,
    readOnly: Boolean = true,
    onIntent: (PlayerDetailsIntent) -> Unit
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(14.dp),
        verticalAlignment = Alignment.Top
    ) {
        PlayerPropertiesDropdown(
            modifier =
                Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
            readOnly = readOnly,
            initialStatus = uiState.selectedStatus ?: UserStatus.ACTIVE,
            options = listOf(UserStatus.ACTIVE, UserStatus.INACTIVE),
            label = "Status"
        ) {
            onIntent(PlayerDetailsIntent.SelectStatus(it.uppercase()))
        }

        PlayerPropertiesDropdown(
            modifier =
                Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
            readOnly = readOnly,
            initialStatus = uiState.selectedRole ?: UserRole.PLAYER,
            options = UserRole.entries,
            label = "Role"
        ) {
            onIntent(PlayerDetailsIntent.SelectRole(it.uppercase()))
        }
    }

    if (!readOnly) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f))
            SubmitButton(
                enabled =
                    !uiState.isLoading &&
                        (
                            uiState.selectedStatus != playerDetails?.status ||
                                uiState.selectedRole != playerDetails?.role
                            ),
                isLoading = uiState.isLoading
            ) { onIntent(PlayerDetailsIntent.UpdatePlayer) }
            Box(
                modifier =
                    Modifier
                        .weight(1f),
                contentAlignment = Alignment.CenterEnd
            ) {
                PlayerThreePointMenu(
                    onRemovePlayer = { onIntent(PlayerDetailsIntent.RemovePlayer) }
                )
            }
        }
    }
}

@Composable
fun LastMatches(onIntent: (PlayerDetailsIntent) -> Unit, playerId: String, lastMatches: List<Match>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Last Matches",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }

    Row {
        LastMatchesList(
            lastMatches = lastMatches,
            playerId = playerId,
            onIntent = onIntent
        )
    }
}

@Composable
fun PlayerThreePointMenu(onRemovePlayer: () -> Unit) {
    var menuExpanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    IconButton(
        onClick = { menuExpanded = true }
    ) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "Menü"
        )
    }

    DropdownMenu(
        expanded = menuExpanded,
        onDismissRequest = { menuExpanded = false }
    ) {
        DropdownMenuItem(
            text = { Text("Remove Player") },
            onClick = {
                menuExpanded = false
                showDialog = true
            }
        )
    }
    if (showDialog) {
        ConfirmAlertDialog(
            text = "Are you sure you want to remove the player from the group?",
            onDismiss = { showDialog = false },
            onConfirm = {
                showDialog = false
                onRemovePlayer()
            }
        )
    }
}

@Composable
fun PlayerShortInfo(playerDetails: PlayerDetails?, groupNames: Map<String, String>) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(14.dp),
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier =
                Modifier
                    .weight(1f)
        ) {
            Text(text = "Nickname", style = MaterialTheme.typography.titleMedium)
            Text(
                text = groupNames[playerDetails?.id] ?: "Unknown",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(text = "Email", style = MaterialTheme.typography.titleMedium)
            Text(
                text = playerDetails?.email ?: "",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        UserImage(
            imageUrl = playerDetails?.avatarUrl,
            size = 90
        )
    }
}

@Composable
fun LastMatchesList(
    lastMatches: List<Match>,
    playerId: String,
    onIntent: (PlayerDetailsIntent) -> Unit,
) {
    Column {
        lastMatches.forEach { match ->
            LastMatchItem(match, playerId, onIntent)
        }
    }
}

@Composable
fun LastMatchItem(lastMatch: Match, playerId: String, onIntent: (PlayerDetailsIntent) -> Unit) {
    val result = lastMatch.result.find { it.userId == playerId }?.result
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable {
                    onIntent(PlayerDetailsIntent.SelectLastMatch(lastMatch.id))
                },
        colors =
            when (result) {
                PlayerMatchResult.WIN -> CardDefaults.cardColors(containerColor = CustomColor.Green)
                PlayerMatchResult.LOSS -> CardDefaults.cardColors(containerColor = CustomColor.Red)
                PlayerMatchResult.DRAW -> CardDefaults.cardColors(containerColor = CustomColor.Gray)
                else ->
                    CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
            }
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = lastMatch.start.toString(),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                when (result) {
                    PlayerMatchResult.WIN ->
                        Icon(
                            imageVector = Icons.Outlined.Star,
                            contentDescription = "Won"
                        )

                    PlayerMatchResult.LOSS ->
                        Icon(
                            imageVector = Icons.Outlined.Cancel,
                            contentDescription = "Lost"
                        )

                    PlayerMatchResult.DRAW ->
                        Icon(
                            imageVector = Icons.Outlined.Handshake,
                            contentDescription = "Draw"
                        )

                    else -> {
                        Icon(
                            imageVector = Icons.Outlined.Schedule,
                            contentDescription = "no result"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PlayerStatsCard(stats: PlayerStats) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier =
                    Modifier
                        .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Matches", style = MaterialTheme.typography.labelLarge)
                Text(
                    text = stats.totalMatches.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier =
                    Modifier
                        .weight(2f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    StatBlock("Wins", stats.wins, CustomColor.Green)
                    StatBlock("Losses", stats.losses, CustomColor.Red)
                    StatBlock("Draws", stats.draws, CustomColor.Gray)
                }

                Spacer(modifier = Modifier.height(14.dp))

                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Winrate", style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = "${calculateWinRate(stats)}%",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun StatBlock(label: String, value: Int, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.titleMedium)
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.bodyLarge.copy(color = color),
            fontWeight = FontWeight.Medium
        )
    }
}

fun calculateWinRate(stats: PlayerStats): String {
    val total = stats.wins + stats.losses + stats.draws
    return if (total == 0) {
        "0.0"
    } else {
        String.format(Locale.GERMAN, "%.1f", stats.wins * 100f / total)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : LabeledEnum> PlayerPropertiesDropdown(
    modifier: Modifier = Modifier,
    initialStatus: T,
    readOnly: Boolean = true,
    label: String,
    options: List<T>,
    onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded && !readOnly },
        modifier = modifier
    ) {
        TextField(
            value = initialStatus.label,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                if (!readOnly) {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                }
            },
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable),
            colors = OutlinedTextFieldDefaults.colors(),
            enabled = !readOnly
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = {
                        Text(text = selectionOption.label)
                    },
                    onClick = {
                        onItemSelected(selectionOption.label)
                        expanded = false
                    }
                )
            }
        }
    }
}