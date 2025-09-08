package com.spruhs.screens.user

import android.net.Uri
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.spruhs.screens.common.UserImage
import com.spruhs.user.presentation.ProfileEffect
import com.spruhs.user.presentation.ProfileIntent
import com.spruhs.user.presentation.ProfileUIState
import com.spruhs.user.presentation.ProfileViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(onLogout: () -> Unit, profileViewModel: ProfileViewModel = koinViewModel()) {
    val profileUIState by profileViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        profileViewModel.effects.collect { effect ->
            when (effect) {
                is ProfileEffect.Logout -> onLogout()
            }
        }
    }

    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                Log.d("ProfileScreen", "Selected image URI: $uri")
            }
        }

    ProfileContent(
        onIntent = profileViewModel::processIntent,
        profileUIState = profileUIState,
        launcher
    )
}

@Composable
fun ProfileContent(
    onIntent: (ProfileIntent) -> Unit,
    profileUIState: ProfileUIState,
    launcher: ManagedActivityResultLauncher<String, Uri?>
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier =
                Modifier
                    .size(256.dp)
            ) {
                UserImage(
                    imageUrl = profileUIState.imageUrl,
                    size = 256
                )

                Button(
                    onClick = { launcher.launch("image/*") },
                    modifier =
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .size(48.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Change Profile Picture",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = profileUIState.newNickName,
                    onValueChange = { onIntent(ProfileIntent.ChangeNewNickname(it)) },
                    label = { Text("Nickname") }
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(onClick = {
                    onIntent(ProfileIntent.ChangeNickname)
                }) {
                    Text(text = "Change")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onIntent(ProfileIntent.Logout) }
            ) {
                Text(text = "Logout")
            }
        }
    }
}