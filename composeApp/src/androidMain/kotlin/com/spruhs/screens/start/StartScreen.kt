package com.spruhs.screens.start

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.spruhs.auth.presentation.StartSideEffect
import com.spruhs.auth.presentation.StartViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun StartScreen(
    onLoggedIn: () -> Unit,
    onLoginFailed: () -> Unit,
    startViewModel: StartViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) {
        startViewModel.effects.collect { effect ->
            when (effect) {
                StartSideEffect.Authenticated -> onLoggedIn()
                StartSideEffect.NotAuthenticated -> onLoginFailed()
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}