package com.spruhs.screens.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.spruhs.auth.presentation.RegisterEffect
import com.spruhs.auth.presentation.RegisterIntent
import com.spruhs.auth.presentation.RegisterUIState
import com.spruhs.auth.presentation.RegisterViewModel
import com.spruhs.screens.common.EmailInput
import com.spruhs.screens.common.SubmitButton
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreen(
    onBackClick: () -> Unit,
    onRegisterSuccess: () -> Unit,
    registerViewModel: RegisterViewModel = koinViewModel()
) {
    val uiState by registerViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        registerViewModel.effects.collect { effect ->
            when (effect) {
                RegisterEffect.RegisterSuccess -> onRegisterSuccess()
                RegisterEffect.Back -> onBackClick()
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        content = { paddingValues ->
            RegisterUserContent(
                modifier = Modifier.padding(paddingValues),
                onIntent = registerViewModel::processIntent,
                uiState = uiState
            )
        }
    )
}

@Composable
fun RegisterUserContent(
    modifier: Modifier = Modifier,
    onIntent: (RegisterIntent) -> Unit,
    uiState: RegisterUIState
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
            text = "Register new user",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = 42.dp),
            color = MaterialTheme.colorScheme.primary
        )

        RegisterFields(
            uiState = uiState,
            onIntent = onIntent
        )

        RegisterButtons(
            uiState = uiState,
            onIntent = onIntent
        )
    }
}

@Composable
fun RegisterFields(onIntent: (RegisterIntent) -> Unit, uiState: RegisterUIState) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmailInput(
            email = uiState.email,
            onEmailChange = { onIntent(RegisterIntent.EmailChanged(it)) },
            error = uiState.error
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(0.8f),
            value = uiState.nickName,
            onValueChange = { onIntent(RegisterIntent.NickNameChanged(it)) },
            label = { Text("Nickname (max. 20 Chars)") },
            singleLine = true
        )

        HorizontalDivider(
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )

        if (uiState.isPasswordValid == false) {
            Text(
                text =
                """
                        Invalid Password.
                        Must be at least 6 characters long and
                        contain at least one uppercase letter,
                        one lowercase letter and one number
                """.trimIndent(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier =
                Modifier
                    .fillMaxWidth(0.8f)
                    .padding(bottom = 8.dp)
            )
        }

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(0.8f),
            value = uiState.password,
            onValueChange = { onIntent(RegisterIntent.PasswordChanged(it)) },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(0.8f),
            value = uiState.repeatedPassword,
            onValueChange = { onIntent(RegisterIntent.RepeatedPasswordChanged(it)) },
            label = { Text("Repeat Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )
    }
}

@Composable
fun RegisterButtons(onIntent: (RegisterIntent) -> Unit, uiState: RegisterUIState) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SubmitButton(
            modifier =
            Modifier
                .padding(bottom = 42.dp)
                .fillMaxWidth(0.5f),
            enabled = uiState.isInputValid && !uiState.isLoading,
            isLoading = uiState.isLoading
        ) { onIntent(RegisterIntent.Register) }
    }

    Button(onClick = { onIntent(RegisterIntent.Back) }) {
        Text(text = "Back")
    }
}