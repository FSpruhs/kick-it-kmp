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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.spruhs.navigation.MainScreens
import com.spruhs.screens.common.UserImage
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KickItTopBar(backIcon: Boolean, navController: NavController) {


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
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onTertiary
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    Text(
                        text = selectedGroupState?.name ?: "Select Group",
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
                    MessageIcon(unreadMessages) {
                        navController.navigate(MainScreens.MessageScreen.route)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Box(
                        modifier =
                            Modifier
                                .clickable { navController.navigate(MainScreens.ProfileScreen.route) }
                                .padding(8.dp)
                    ) {
                        UserImage(userState.data?.imageUrl, 48)
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