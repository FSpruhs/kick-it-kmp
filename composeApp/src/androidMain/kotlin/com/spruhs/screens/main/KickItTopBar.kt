package com.spruhs.screens.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.spruhs.main.TopBarEffect
import com.spruhs.main.TopBarIntent
import com.spruhs.main.TopBarUIState
import com.spruhs.main.TopBarViewModel
import com.spruhs.screens.common.UserImage
import org.koin.androidx.compose.koinViewModel

@Composable
fun KickItTopBar(
    backIcon: Boolean,
    onBackClick: () -> Unit,
    onMessageClick: () -> Unit,
    onProfileClick: () -> Unit,
    topBarViewModel: TopBarViewModel = koinViewModel()
) {
    val uiState by topBarViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        topBarViewModel.effects.collect { effect ->
            when (effect) {
                TopBarEffect.Back -> onBackClick()
                TopBarEffect.Messages -> onMessageClick()
                TopBarEffect.Profile -> onProfileClick()
            }
        }
    }

    KickItTopBarContent(
        backIcon,
        uiState,
        topBarViewModel::processIntent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KickItTopBarContent(
    backIcon: Boolean,
    uiState: TopBarUIState,
    onIntent: (TopBarIntent) -> Unit
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (backIcon) {
                        BackIcon(onIntent)
                    }

                    Text(
                        text = uiState.selectedGroupName ?: "Select Group",
                        style =
                        MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.onTertiary,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    MessageIcon(uiState.unreadMessage) {
                        onIntent(TopBarIntent.Messages)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Box(
                        modifier =
                        Modifier
                            .clickable { onIntent(TopBarIntent.Profile) }
                            .padding(8.dp)
                    ) {
                        UserImage(uiState.imageUrl, 48)
                    }
                }
            }
        },
        colors =
        TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.tertiary
        )
    )
}

@Composable
fun BackIcon(onIntent: (TopBarIntent) -> Unit) {
    IconButton(onClick = { onIntent(TopBarIntent.Back) }) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = MaterialTheme.colorScheme.onTertiary
        )
    }
    Spacer(modifier = Modifier.width(8.dp))
}

@Composable
fun MessageIcon(unreadMessages: Int, onClick: () -> Unit) {
    IconButton(
        onClick = { onClick() },
        modifier = Modifier.padding(4.dp)
    ) {
        BadgedBox(
            badge = {
                if (unreadMessages > 0) {
                    Badge {
                        Text(
                            text = unreadMessages.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1
                        )
                    }
                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = "Messages",
                tint = MaterialTheme.colorScheme.onTertiary
            )
        }
    }
}