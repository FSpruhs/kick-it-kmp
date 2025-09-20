package com.spruhs.screens.match

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.spruhs.match.application.PlayerTeam
import com.spruhs.match.presentation.MatchResultDetailIntent
import com.spruhs.match.presentation.MatchResultDetailUIState
import com.spruhs.match.presentation.MatchResultDetailViewModel
import com.spruhs.permission.PermissionManager
import com.spruhs.screens.common.CancelButton
import com.spruhs.screens.common.ConfirmAlertDialog
import com.spruhs.screens.common.RoleBasedVisibility
import com.spruhs.ui.theme.CustomColor
import com.spruhs.user.application.SelectedGroup
import com.spruhs.user.application.UserRole
import org.koin.androidx.compose.koinViewModel

@Composable
fun MatchResultDetailScreen(
    matchId: String,
    onEnterResultClick: (String) -> Unit,
    onBack: () -> Unit,
    matchResultDetailViewModel: MatchResultDetailViewModel = koinViewModel()
) {
    val matchResultDetailUIState by matchResultDetailViewModel.uiState.collectAsStateWithLifecycle()

    if (matchResultDetailUIState.playerResults.isEmpty()) {
        ResultNotEnteredYetContent(
            selectedGroup = matchResultDetailUIState.selectedGroup,
            onBack = onBack,
            onIntent = matchResultDetailViewModel::processIntent
        )
    } else {
        MatchResultContent(
            matchResultDetailUIState = matchResultDetailUIState,
            onIntent = matchResultDetailViewModel::processIntent
        )
    }
}

@Composable
fun MatchResultContent(
    matchResultDetailUIState: MatchResultDetailUIState,
    onIntent: (MatchResultDetailIntent) -> Unit
) {
    Column(modifier = Modifier.padding(top = 16.dp)) {
        Text(
            text =
            if (matchResultDetailUIState.isDraw) {
                "Draw"
            } else {
                "Winner-Team"
            },
            style = MaterialTheme.typography.titleMedium,
            modifier =
            Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 4.dp)
        )

        PlayerChipArea(
            color =
            if (matchResultDetailUIState.isDraw) {
                CustomColor.Gray
            } else {
                CustomColor.Green
            },
            players =
            if (matchResultDetailUIState.winnerTeam == PlayerTeam.A) {
                matchResultDetailUIState.playerResults
                    .filter { it.team == PlayerTeam.A }
                    .map { it.userId }
            } else {
                matchResultDetailUIState
                    .playerResults
                    .filter { it.team == PlayerTeam.B }
                    .map { it.userId }
            },
            groupNameList = matchResultDetailUIState.groupNameList
        )

        HorizontalDivider(
            modifier = Modifier.padding(bottom = 8.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )

        Text(
            text =
            if (matchResultDetailUIState.isDraw) {
                "Draw"
            } else {
                "Looser-Team"
            },
            style = MaterialTheme.typography.titleMedium,
            modifier =
            Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 4.dp)
        )

        PlayerChipArea(
            color =
            if (matchResultDetailUIState.isDraw) {
                CustomColor.Gray
            } else {
                CustomColor.Red
            },
            players =
            if (matchResultDetailUIState.winnerTeam == PlayerTeam.A) {
                matchResultDetailUIState
                    .playerResults
                    .filter { it.team == PlayerTeam.B }
                    .map { it.userId }
            } else {
                matchResultDetailUIState
                    .playerResults
                    .filter { it.team == PlayerTeam.A }
                    .map { it.userId }
            },
            groupNameList = matchResultDetailUIState.groupNameList
        )

        var menuExpanded by remember { mutableStateOf(false) }
        var showDialog by remember { mutableStateOf(false) }

        RoleBasedVisibility(
            matchResultDetailUIState.selectedGroup?.role,
            "matchResultDetailScreen:changeResultButton"
        ) {
            CancelButton(
                menuExpanded = menuExpanded,
                setShowDialog = { showDialog = it },
                setMenuExpanded = { menuExpanded = it },
                text = "Change result"
            )
        }

        if (showDialog) {
            ConfirmAlertDialog(
                text = "Are you sure you want to change the result?",
                onDismiss = { showDialog = false },
                onConfirm = {
                    showDialog = false
                    onIntent(MatchResultDetailIntent.ChangeResult)
                }
            )
        }
    }
}

@Composable
fun ResultNotEnteredYetContent(
    selectedGroup: SelectedGroup?,
    onBack: () -> Unit,
    onIntent: (MatchResultDetailIntent) -> Unit
) {
    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (PermissionManager.hasPermission(
                selectedGroup?.role ?: UserRole.PLAYER,
                "matchResultDetailScreen:enterResultButton"
            )
        ) {
            Button(
                modifier =
                Modifier
                    .width(200.dp)
                    .height(56.dp),
                onClick = { onIntent(MatchResultDetailIntent.EnterResult) }
            ) { Text(text = "Enter result") }
        } else {
            Text(text = "Result not entered yet", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier =
                Modifier
                    .width(200.dp)
                    .height(56.dp),
                onClick = { onBack() }
            ) { Text("Back") }
        }
    }
}