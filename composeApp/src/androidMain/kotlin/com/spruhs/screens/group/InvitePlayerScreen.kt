package com.spruhs.screens.group

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.spruhs.group.presentation.InvitePlayerEffect
import com.spruhs.group.presentation.InvitePlayerIntent
import com.spruhs.group.presentation.InvitePlayerUIState
import com.spruhs.group.presentation.InvitePlayerViewModel
import com.spruhs.screens.common.EmailInput
import com.spruhs.screens.common.SubmitButton
import org.koin.androidx.compose.koinViewModel

@Composable
fun InvitePlayerScreen(
    onPlayerInvitedSuccess: () -> Unit,
    invitePlayerViewModel: InvitePlayerViewModel = koinViewModel()
) {
    val uiState by invitePlayerViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        invitePlayerViewModel.effects.collect { effect ->
            when (effect) {
                InvitePlayerEffect.PlayerInvited -> {
                    Toast
                        .makeText(
                            context,
                            "Player invited!",
                            Toast.LENGTH_SHORT
                        ).show()
                    onPlayerInvitedSuccess()
                }

                is InvitePlayerEffect.ShowError -> {
                    Toast.makeText(context, uiState.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        content = { paddingValues ->
            InvitePlayerContent(
                modifier = Modifier.padding(paddingValues),
                uiState = uiState,
                onIntent = invitePlayerViewModel::processIntent
            )
        }
    )
}

@Composable
fun InvitePlayerContent(
    modifier: Modifier = Modifier,
    uiState: InvitePlayerUIState,
    onIntent: (InvitePlayerIntent) -> Unit
) {
    Column(
        modifier =
        modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Enter players email",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = 42.dp),
            color = MaterialTheme.colorScheme.primary
        )

        EmailInput(
            label = "Player Email",
            email = uiState.email,
            onEmailChange = { onIntent(InvitePlayerIntent.EmailChanged(it)) },
            error = uiState.error
        )

        SubmitButton(
            modifier =
            Modifier
                .padding(bottom = 42.dp)
                .fillMaxWidth(0.5f),
            isLoading = uiState.isLoading,
            enabled = uiState.email.isNotEmpty() && !uiState.isLoading
        ) { onIntent(InvitePlayerIntent.InvitePlayer) }
    }
}