package com.spruhs.screens.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import com.spruhs.auth.presentation.LoginIntent
import com.spruhs.auth.presentation.LoginSideEffect
import com.spruhs.auth.presentation.LoginUIState
import com.spruhs.auth.presentation.LoginViewModel
import com.spruhs.screens.common.EmailInput
import com.spruhs.screens.common.PasswordInput
import com.spruhs.screens.common.SubmitButton
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    onLoggedIn: () -> Unit,
    onRegisterClick: () -> Unit,
    loginViewModel: LoginViewModel = koinViewModel()
) {
    val uiState by loginViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        loginViewModel.effects.collect { effect ->
            when (effect) {
                LoginSideEffect.LoginSuccess -> onLoggedIn()
                LoginSideEffect.Register -> onRegisterClick()
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        content = { paddingValues ->
            LoginContent(
                modifier = Modifier.padding(paddingValues),
                uiState = uiState,
                onIntent = loginViewModel::processIntent
            )
        }
    )
}

@Composable
fun LoginContent(
    modifier: Modifier = Modifier,
    uiState: LoginUIState,
    onIntent: (LoginIntent) -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
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
                text = "Login",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(top = 42.dp),
                color = MaterialTheme.colorScheme.primary
            )

            LoginFields(
                uiState = uiState,
                onIntent = onIntent
            )

            LoginButtons(
                uiState = uiState,
                onIntent = onIntent
            )
        }
    }
}

@Composable
fun LoginButtons(
    uiState: LoginUIState,
    onIntent: (LoginIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SubmitButton(
            modifier =
            modifier
                .padding(bottom = 42.dp)
                .fillMaxWidth(0.5f),
            enabled = !uiState.isLoading && uiState.isInputValid,
            isLoading = uiState.isLoading
        ) {
            onIntent(LoginIntent.Login)
        }

        Button(
            onClick = { onIntent(LoginIntent.Register) }
        ) {
            Text(text = "Register")
        }
    }
}

@Composable
fun LoginFields(
    uiState: LoginUIState,
    onIntent: (LoginIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (uiState.loginError && !uiState.isLoading) {
            Text(
                text = "E-Mail or Password incorrect.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(bottom = 8.dp)
            )
        }

        EmailInput(
            email = uiState.email,
            onEmailChange = { onIntent(LoginIntent.EmailChanged(it)) },
            error = uiState.error
        )

        PasswordInput(
            password = uiState.password,
            onPasswordChange = { onIntent(LoginIntent.PasswordChanged(it)) }
        )
    }
}