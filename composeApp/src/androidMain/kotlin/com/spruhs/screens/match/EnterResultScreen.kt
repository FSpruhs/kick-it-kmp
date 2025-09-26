package com.spruhs.screens.match

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.spruhs.match.presentation.EnterMatchResultEffect
import com.spruhs.match.presentation.EnterMatchResultIntent
import com.spruhs.match.presentation.EnterMatchResultUIState
import com.spruhs.match.presentation.EnterMatchResultViewModel
import com.spruhs.match.presentation.Side
import com.spruhs.screens.common.SubmitButton
import com.spruhs.ui.theme.CustomColor
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun EnterResultScreen(
    matchId: String,
    onResultEntered: () -> Unit,
    enterMatchResultViewModel: EnterMatchResultViewModel = koinViewModel(
        parameters = { parametersOf(matchId) }
    )
) {
    val uiState by enterMatchResultViewModel.uiState.collectAsState()

    HandleEnterResultEffect(
        effects = enterMatchResultViewModel.effects,
        onResultEntered = onResultEntered
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        content = { paddingValues ->
            EnterResultContent(
                modifier = Modifier.padding(paddingValues),
                uiState = uiState,
                onIntent = enterMatchResultViewModel::processIntent
            )
        }
    )
}

@Composable
fun HandleEnterResultEffect(
    effects: Flow<EnterMatchResultEffect>,
    context: Context = LocalContext.current,
    onResultEntered: () -> Unit
) {
    LaunchedEffect(Unit) {
        effects.collect { effect ->
            when (effect) {
                EnterMatchResultEffect.ResultEntered -> {
                    Toast
                        .makeText(
                            context,
                            "Result entered!",
                            Toast.LENGTH_SHORT
                        ).show()
                    onResultEntered()
                }

                is EnterMatchResultEffect.ShowError -> {
                    Toast
                        .makeText(
                            context,
                            effect.message,
                            Toast.LENGTH_SHORT
                        ).show()
                }
            }
        }
    }
}

@Composable
fun EnterResultContent(
    modifier: Modifier = Modifier,
    uiState: EnterMatchResultUIState,
    onIntent: (EnterMatchResultIntent) -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier =
            Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            PlayerAreaContent(
                uiState = uiState,
                resultName = "Winner",
                teamName = "Team-A",
                color = CustomColor.Green,
                teamSide = Side.LEFT,
                team = uiState.teamAPlayers,
                onIntent = onIntent
            )

            PlayerSelectionContent(
                uiState = uiState,
                onIntent = onIntent
            )

            PlayerAreaContent(
                uiState = uiState,
                resultName = "Looser",
                teamName = "Team-B",
                color = CustomColor.Red,
                teamSide = Side.RIGHT,
                team = uiState.teamBPlayers,
                onIntent = onIntent
            )
        }
    }
}

@Composable
fun PlayerSelectionContent(
    uiState: EnterMatchResultUIState,
    onIntent: (EnterMatchResultIntent) -> Unit
) {
    Column(
        modifier =
        Modifier
            .width(124.dp)
            .fillMaxHeight()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SubmitButton(
            enabled =
            uiState.teamAPlayers.isNotEmpty() &&
                uiState.teamBPlayers.isNotEmpty() &&
                !uiState.isLoading,
            isLoading = uiState.isLoading,
            onSubmitClick = { onIntent(EnterMatchResultIntent.EnterResult) }
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        NavigationButtons(uiState, onIntent)

        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = uiState.isDraw,
                onCheckedChange = { onIntent(EnterMatchResultIntent.UpdateIsDraw(it)) }
            )
            Text(text = "Draw")
        }

        HorizontalDivider(
            modifier =
            Modifier
                .padding(bottom = 8.dp)
        )

        Text("Players:")

        LazyColumn(modifier = Modifier.fillMaxHeight()) {
            items(uiState.noTeamPlayers.toList()) { player ->
                PlayerChipItem(
                    player = player,
                    selected =
                    player == uiState.selectedPlayer &&
                        uiState.selectedSide == Side.MIDDLE,
                    onClick = {
                        onIntent(EnterMatchResultIntent.SelectPlayer(Side.MIDDLE, player))
                    },
                    uiState = uiState
                )
            }
        }
    }
}

@Composable
fun RowScope.PlayerAreaContent(
    resultName: String,
    teamName: String,
    uiState: EnterMatchResultUIState,
    color: Color,
    teamSide: Side,
    team: Set<String>,
    onIntent: (EnterMatchResultIntent) -> Unit
) {
    Column(
        modifier =
        Modifier
            .weight(1f)
            .fillMaxHeight()
    ) {
        Text(
            text = if (uiState.isDraw) teamName else resultName,
            modifier =
            Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 4.dp),
            style = MaterialTheme.typography.titleMedium
        )

        Column(
            modifier =
            Modifier
                .fillMaxSize()
                .background(
                    if (uiState.isDraw) CustomColor.Gray else color,
                    RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
        ) {
            LazyColumn {
                items(team.toList()) { player ->
                    PlayerChipItem(
                        player = player,
                        selected =
                        player == uiState.selectedPlayer &&
                            uiState.selectedSide == teamSide,
                        onClick = {
                            onIntent(
                                EnterMatchResultIntent.SelectPlayer(teamSide, player)
                            )
                        },
                        uiState = uiState
                    )
                }
            }
        }
    }
}

@Composable
fun NavigationButtons(
    enterMatchResultUIState: EnterMatchResultUIState,
    onIntent: (EnterMatchResultIntent) -> Unit
) {
    NavigationButton(
        enterMatchResultUIState = enterMatchResultUIState,
        imageVector = Icons.Default.ArrowCircleLeft,
        contentDescription = "Move to Left",
        size = 64,
        moveTeam = Side.LEFT,
        onIntent = onIntent
    )

    Spacer(modifier = Modifier.height(4.dp))

    NavigationButton(
        enterMatchResultUIState = enterMatchResultUIState,
        imageVector = Icons.Default.Cancel,
        contentDescription = "Move to Middle",
        size = 64,
        moveTeam = Side.MIDDLE,
        onIntent = onIntent
    )

    Spacer(modifier = Modifier.height(4.dp))

    NavigationButton(
        enterMatchResultUIState = enterMatchResultUIState,
        imageVector = Icons.Default.ArrowCircleRight,
        contentDescription = "Move to Right",
        size = 64,
        moveTeam = Side.RIGHT,
        onIntent = onIntent
    )
}

@Composable
fun NavigationButton(
    enterMatchResultUIState: EnterMatchResultUIState,
    imageVector: ImageVector,
    contentDescription: String,
    size: Int,
    moveTeam: Side,
    onIntent: (EnterMatchResultIntent) -> Unit
) {
    IconButton(
        onClick = { onIntent(EnterMatchResultIntent.MovePlayer(moveTeam)) },
        modifier = Modifier.size(size.dp),
        enabled = enterMatchResultUIState.selectedPlayer != null
    ) {
        Icon(
            modifier = Modifier.size(size.dp),
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint =
            if (enterMatchResultUIState.selectedPlayer != null) {
                MaterialTheme.colorScheme.primary
            } else {
                Color.Gray
            }
        )
    }
}

@Composable
fun PlayerChipItem(
    uiState: EnterMatchResultUIState,
    player: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    AssistChip(
        colors =
        AssistChipDefaults.assistChipColors(
            containerColor =
            if (selected) {
                MaterialTheme.colorScheme.tertiary
            } else {
                Color.Transparent
            },
            labelColor = if (selected) MaterialTheme.colorScheme.onTertiary else Color.Black
        ),
        onClick = { onClick() },
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        shape = RoundedCornerShape(8.dp),
        label = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = uiState.playerNames[player] ?: "Unknown name",
                    modifier = Modifier.padding(8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    )
}