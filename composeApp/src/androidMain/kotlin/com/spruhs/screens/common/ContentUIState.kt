package com.spruhs.screens.common

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.spruhs.BaseUIState

@Composable
fun <T : BaseUIState<T>> ContentUIState(
    contentUIState: BaseUIState<T>,
    content: @Composable () -> Unit
) {
    when {
        contentUIState.isLoading -> {
            CircularProgressIndicator()
        }

        contentUIState.error != null -> {
            Text(text = contentUIState.error ?: "Unknown Error")
        }

        else -> {
            content()
        }
    }
}