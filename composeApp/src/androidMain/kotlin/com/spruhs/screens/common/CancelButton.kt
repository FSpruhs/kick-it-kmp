package com.spruhs.screens.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun CancelButton(
    menuExpanded: Boolean,
    setShowDialog: (Boolean) -> Unit,
    setMenuExpanded: (Boolean) -> Unit,
    text: String
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = { setMenuExpanded(true) }
        ) {
            Icon(
                imageVector = Icons.Default.RemoveCircle,
                contentDescription = "Menü",
                tint = MaterialTheme.colorScheme.error
            )

            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { setMenuExpanded(false) }
            ) {
                DropdownMenuItem(
                    text = { Text(text) },
                    onClick = {
                        setMenuExpanded(false)
                        setShowDialog(true)
                    }
                )
            }
        }
    }
}