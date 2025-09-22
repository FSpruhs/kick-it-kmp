package com.spruhs.screens.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EmailInput(
    email: String,
    label: String = "Email",
    onEmailChange: (String) -> Unit,
    error: String? = null
) {
    Column(
        modifier = Modifier.fillMaxWidth(0.8f)
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text(label) },
            singleLine = true,
            isError = error != null,
            modifier = Modifier.fillMaxWidth()
        )

        if (!error.isNullOrEmpty()) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}