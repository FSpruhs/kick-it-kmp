package com.spruhs.screens.common

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.outlined.Pending
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.spruhs.match.application.PlayerStatus
import com.spruhs.ui.theme.CustomColor

@Composable
fun PlayerMatchStatusIcon(status: PlayerStatus?, size: Int) {
    when (status) {
        PlayerStatus.CADRE -> {
            Icon(
                imageVector = Icons.Rounded.Check,
                tint = CustomColor.Green,
                contentDescription = "Cadre Status",
                modifier = Modifier.size(size.dp)
            )
        }

        PlayerStatus.WAITING_BENCH -> {
            Icon(
                imageVector = Icons.Default.HourglassEmpty,
                tint = CustomColor.Gray,
                contentDescription = "Waiting on Bench",
                modifier = Modifier.size(size.dp)
            )
        }

        PlayerStatus.DEREGISTERED -> {
            Icon(
                imageVector = Icons.Default.Close,
                tint = CustomColor.Red,
                contentDescription = "Rejected Status",
                modifier = Modifier.size(size.dp)
            )
        }

        null -> {
            Icon(
                imageVector = Icons.Outlined.Pending,
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = "Unknown Status",
                modifier = Modifier.size(size.dp)
            )
        }
    }
}