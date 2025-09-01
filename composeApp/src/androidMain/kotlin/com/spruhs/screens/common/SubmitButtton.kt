package com.spruhs.screens.common

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SubmitButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    onSubmitClick: () -> Unit
) {
    Button(
        colors =
        ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = modifier,
        enabled = enabled,
        onClick = { onSubmitClick() }
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator()
            }

            else -> Text("Submit", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}