package com.spruhs.screens.start

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.spruhs.auth.presentation.StartViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun StartScreen(
    onLoggedIn: () -> Unit,
    onLoginFailed: () -> Unit,
    startViewModel: StartViewModel = koinViewModel()
) {
    val startUIState by startViewModel.startUIState.collectAsStateWithLifecycle()

    var hasChecked by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(2000)
        hasChecked = true
    }

    LaunchedEffect(hasChecked, startUIState.authenticated) {
        if (hasChecked) {
            if (startUIState.authenticated == true) {
                onLoggedIn()
            } else {
                onLoginFailed()
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