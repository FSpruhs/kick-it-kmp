package com.spruhs.screens.match

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.spruhs.match.presentation.MatchIntent
import com.spruhs.match.presentation.MatchUIState
import com.spruhs.match.presentation.MatchViewModel
import com.spruhs.screens.common.RoleBasedVisibility
import com.spruhs.screens.group.LastMatchItem
import com.spruhs.screens.user.UpcomingMatchesItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun MatchScreen(
    setBackIcon: (Boolean) -> Unit,
    onPlanMatchClick: () -> Unit,
    onUpcomingMatchClick: (String) -> Unit,
    onLastMatchClick: (String) -> Unit,
    matchViewModel: MatchViewModel = koinViewModel()
) {
    val matchUIState by matchViewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        setBackIcon(false)
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
                MatchContent(
                    matchUIState = matchUIState,
                    onIntent = matchViewModel::processIntent
                )
            }
        },
        floatingActionButton = {
            RoleBasedVisibility(
                matchUIState.selectedGroup?.role,
                "matchScreen:planMatchButton"
            ) {
                FloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = { onPlanMatchClick() }
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
fun MatchContent(matchUIState: MatchUIState, onIntent: (MatchIntent) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Upcoming matches",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        HorizontalDivider(
            modifier = Modifier.padding(bottom = 8.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )
        when {
            matchUIState.isLoading -> {
                CircularProgressIndicator()
            }

            matchUIState.upcomingMatches.isEmpty() -> {
                Text(text = "No upcoming matches")
            }

            else -> {
                LazyColumn {
                    items(matchUIState.upcomingMatches) { match ->
                        UpcomingMatchesItem(
                            upcomingMatchPreview = match,
                            groups = matchUIState.groups,
                            onMatchClick = { onIntent(MatchIntent.SelectMatch(it)) },
                            userId = matchUIState.userId ?: ""
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Last Matches",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        HorizontalDivider(
            modifier = Modifier.padding(bottom = 8.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )

        when {
            matchUIState.isLoading -> {
                CircularProgressIndicator()
            }
            matchUIState.lastMatches.isEmpty() -> {
                Text(text = "No last matches")
            }
            else -> {
                LazyColumn {
                    items(matchUIState.lastMatches) { match ->
                        LastMatchItem(
                            lastMatch = match,
                            playerId = matchUIState.userId ?: "",
                            onClick = { onIntent(MatchIntent.SelectMatch(it)) }
                        )
                    }
                }
            }
        }
    }
}