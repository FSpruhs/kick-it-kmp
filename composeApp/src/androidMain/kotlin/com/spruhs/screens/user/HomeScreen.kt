package com.spruhs.screens.user

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.spruhs.match.application.Match
import com.spruhs.match.application.PlayerStatus
import com.spruhs.screens.common.PlayerMatchStatusIcon
import com.spruhs.user.application.UserGroupInfo
import com.spruhs.user.presentation.HomeEffect
import com.spruhs.user.presentation.HomeIntent
import com.spruhs.user.presentation.HomeUIState
import com.spruhs.user.presentation.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    onMatchClick: (String) -> Unit,
    setBackIcon: (Boolean) -> Unit,
    homeViewModel: HomeViewModel = koinViewModel()
) {
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        setBackIcon(false)
        homeViewModel.effects.collect { effect ->
            when (effect) {
                is HomeEffect.MatchSelected -> onMatchClick(effect.matchId)
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        content = { paddingValues ->
            Column(
                modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 6.dp)
            ) {
                UpcomingMatchesContent(
                    modifier = Modifier.weight(1f),
                    onIntent = homeViewModel::processIntent,
                    homeUiState = uiState
                )
            }
        }
    )
}

@Composable
fun UpcomingMatchesContent(
    modifier: Modifier = Modifier,
    onIntent: (HomeIntent) -> Unit,
    homeUiState: HomeUIState
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Upcoming Matches",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        HorizontalDivider(
            modifier = Modifier.padding(bottom = 8.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )

        when {
            homeUiState.isLoading -> {
                CircularProgressIndicator()
            }
            homeUiState.error != null -> {
                Text(text = homeUiState.error ?: "Unknown error")
            }
            homeUiState.upcomingMatches.isEmpty() -> {
                Text(text = "No upcoming matches")
            }
            else -> {
                LazyColumn {
                    items(homeUiState.upcomingMatches) { match ->
                        UpcomingMatchesItem(
                            upcomingMatchPreview = match,
                            groups = homeUiState.groups,
                            userId = homeUiState.userId ?: "",
                            onIntent = onIntent
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UpcomingMatchesItem(
    upcomingMatchPreview: Match,
    groups: Map<String, UserGroupInfo>,
    onIntent: (HomeIntent) -> Unit,
    userId: String
) {
    Card(
        colors =
        CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = LocalIndication.current
            ) {
                onIntent(HomeIntent.SelectMatch(upcomingMatchPreview.id))
            }
    ) {
        Row(
            modifier =
            Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(6.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier =
                Modifier
                    .weight(0.5f)
                    .padding(end = 8.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RegistrationDisplay(
                    size = 48,
                    actual = upcomingMatchPreview.cadre.size,
                    from = upcomingMatchPreview.maxPlayers
                )
            }

            Column(modifier = Modifier.weight(2f)) {
                Text(
                    text = upcomingMatchPreview.start.toString(),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(text = groups[upcomingMatchPreview.groupId]?.name ?: "Unknown Group")
            }

            Column(
                modifier =
                Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                PlayerStatusIcon(calculatePlayerStatus(upcomingMatchPreview, userId))
            }
        }
    }
}

private fun calculatePlayerStatus(match: Match, userId: String) = when {
    match.cadre.find { it == userId } != null -> PlayerStatus.CADRE
    match.deregistered.find { it == userId } != null -> PlayerStatus.DEREGISTERED
    match.waitingBench.find { it == userId } != null -> PlayerStatus.WAITING_BENCH

    else -> null
}

@Composable
fun RegistrationDisplay(size: Int, actual: Int, from: Int) {
    Box(
        contentAlignment = Alignment.Center,
        modifier =
        Modifier
            .size(size.dp)
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = CircleShape
            ).border(1.dp, MaterialTheme.colorScheme.tertiary, CircleShape)
    ) {
        Text(
            text = "$actual/$from",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}

@Composable
fun PlayerStatusIcon(status: PlayerStatus?) {
    Box(
        modifier =
        Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        PlayerMatchStatusIcon(
            status = status,
            size = 24
        )
    }
}