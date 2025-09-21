package com.spruhs.screens.user

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.spruhs.user.application.UserGroupInfo
import com.spruhs.user.presentation.SelectGroupIntent
import com.spruhs.user.presentation.SelectGroupUIState
import com.spruhs.user.presentation.SelectGroupViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SelectGroupScreen(
    selectGroupViewModel: SelectGroupViewModel = koinViewModel()
) {
    val selectGroupUIState by selectGroupViewModel.uiState.collectAsStateWithLifecycle()
    GroupContent(
        modifier = Modifier.fillMaxSize(),
        selectGroupUIState = selectGroupUIState,
        onIntent = selectGroupViewModel::processIntent
    )
}

@Composable
fun GroupContent(
    modifier: Modifier = Modifier,
    selectGroupUIState: SelectGroupUIState,
    onIntent: (SelectGroupIntent) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GroupHeader(onCreateGroupClick = { onIntent(SelectGroupIntent.CreateGroup) })
        HorizontalDivider(
            modifier = Modifier.padding(bottom = 8.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )
        GroupListContent(selectGroupUIState, onIntent)
    }
}

@Composable
fun GroupHeader(onCreateGroupClick: () -> Unit) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
    ) {
        Text(
            text = "Groups",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.Center),
            color = MaterialTheme.colorScheme.primary
        )
        Button(
            onClick = { onCreateGroupClick() },
            modifier =
                Modifier
                    .align(Alignment.CenterEnd)
                    .size(48.dp)
                    .clip(CircleShape),
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun GroupListContent(
    selectGroupUIState: SelectGroupUIState,
    onIntent: (SelectGroupIntent) -> Unit
) {
    val id = selectGroupUIState.id


    when {
        selectGroupUIState.isLoading -> {
            CircularProgressIndicator()
        }

        selectGroupUIState.groups.isEmpty() -> {
            Text(text = "No groups found")
        }

        else -> {
            LazyColumn {
                items(selectGroupUIState.groups) { group ->
                    GroupItem(
                        group,
                        isSelected = group.id == id,
                        onClick = { onIntent(SelectGroupIntent.SelectGroup(it.id)) }
                    )
                }
            }
        }

    }
}

@Composable
fun GroupItem(userGroupInfo: UserGroupInfo, isSelected: Boolean, onClick: (UserGroupInfo) -> Unit) {
    Card(
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = LocalIndication.current
                ) {
                    onClick(userGroupInfo)
                }
                .border(
                    width = 2.dp,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                    shape = MaterialTheme.shapes.medium
                )
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = userGroupInfo.name, style = MaterialTheme.typography.titleSmall)
            }
        }
    }
}