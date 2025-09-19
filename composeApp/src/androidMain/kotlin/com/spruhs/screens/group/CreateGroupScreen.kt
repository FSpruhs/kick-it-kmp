package com.spruhs.screens.group

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.spruhs.group.presentation.CreateGroupEffect
import com.spruhs.group.presentation.CreateGroupIntent
import com.spruhs.group.presentation.CreateGroupUIState
import com.spruhs.group.presentation.CreateGroupViewModel
import com.spruhs.screens.common.SubmitButton
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateGroupScreen(
    onCreateGroupSuccess: () -> Unit,
    createGroupViewModel: CreateGroupViewModel = koinViewModel()
) {
    val createGroupUIState by createGroupViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        createGroupViewModel.effects.collect { effect ->
            when (effect) {
                CreateGroupEffect.GroupCreated -> {
                    Toast
                        .makeText(
                            context,
                            "Group created!",
                            Toast.LENGTH_SHORT
                        ).show()
                    onCreateGroupSuccess()
                }
            }

            if (createGroupUIState.error != null) {
                Toast
                    .makeText(
                        context,
                        createGroupUIState.error,
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        content = { paddingValues ->
            CreateGroupContent(
                modifier = Modifier.padding(paddingValues),
                createGroupUIState = createGroupUIState,
                onIntent = createGroupViewModel::processIntent
            )
        }
    )
}

@Composable
fun CreateGroupContent(
    modifier: Modifier = Modifier,
    createGroupUIState: CreateGroupUIState,
    onIntent: (CreateGroupIntent) -> Unit
) {
    Column(
        modifier =
        modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Enter new group name",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = 42.dp),
            color = MaterialTheme.colorScheme.primary
        )

        OutlinedTextField(
            value = createGroupUIState.newGroupName,
            onValueChange = {
                onIntent(CreateGroupIntent.NewGroupNameChanged(it))
            },
            label = { Text("Group Name (max. ${createGroupUIState.maxChars} Chars)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        SubmitButton(
            modifier =
            Modifier
                .padding(bottom = 42.dp)
                .fillMaxWidth(0.5f),
            isLoading = createGroupUIState.isLoading,
            enabled = createGroupUIState.newGroupName.length >= 2 && !createGroupUIState.isLoading,
            onSubmitClick = { onIntent(CreateGroupIntent.CreateGroup) }
        )
    }
}