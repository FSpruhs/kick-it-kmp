package com.spruhs.screens.group

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.spruhs.screens.common.SubmitButton
import org.koin.androidx.compose.koinViewModel

@Composable
fun InvitePlayerScreen(
    onPlayerInvitedSuccess: () -> Unit,
    invitePlayerViewModel: InvitePlayerViewModel = koinViewModel()
) {
    val inviteUserUIState by invitePlayerViewModel.uiState.collectAsStateWithLifecycle()
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
            }

            if (inviteUserUIState.error != null) {
                Toast
                    .makeText(
                        context,
                        inviteUserUIState.error,
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        content = { paddingValues ->
            InvitePlayerContent(
                modifier = Modifier.padding(paddingValues),
                inviteUserUIState = inviteUserUIState,
                onIntent = invitePlayerViewModel::processIntent
            )
        }
    )
}

@Composable
fun InvitePlayerContent(
    modifier: Modifier = Modifier,
    inviteUserUIState: InvitePlayerUIState,
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
            email = inviteUserUIState.playerEmail,
            onEmailChange = { onIntent(InvitePlayerIntent.PlayerEmailChanged(it)) }
        )
        SubmitButton(
            modifier =
            Modifier
                .padding(bottom = 42.dp)
                .fillMaxWidth(0.5f),
            isLoading = inviteUserUIState.isLoading,
            enabled = inviteUserUIState.playerEmail.isNotEmpty() && !inviteUserUIState.isLoading
        ) { onIntent(InvitePlayerIntent.InvitePlayer) }
    }
}

@Composable
fun EmailInput(email: String, label: String = "Email", onEmailChange: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(0.8f)
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text(label) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}