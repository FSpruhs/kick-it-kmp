package com.spruhs.screens.main

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun BottomNavigationBar(
    selectedItem: String,
    onHomeClick: (String) -> Unit,
    onGroupClick: (String) -> Unit,
    onMatchClick: (String) -> Unit
) {
    NavigationBar(
        modifier =
            Modifier
                .fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) {
        KickItNavigationItem(
            itemName = BottomNavigationItem.Home.name,
            imageVector = Icons.Default.Home,
            contentDescription = "Home Icon",
            onClick = onHomeClick,
            selectedItem = selectedItem
        )

        KickItNavigationItem(
            itemName = BottomNavigationItem.Group.name,
            imageVector = Icons.Default.Groups,
            contentDescription = "Groups Icon",
            onClick = onGroupClick,
            selectedItem = selectedItem
        )

        KickItNavigationItem(
            itemName = BottomNavigationItem.Match.name,
            imageVector = Icons.Default.SportsSoccer,
            contentDescription = "Matches Icon",
            onClick = onMatchClick,
            selectedItem = selectedItem
        )
    }
}

@Composable
fun RowScope.KickItNavigationItem(
    itemName: String,
    imageVector: ImageVector,
    contentDescription: String,
    onClick: (String) -> Unit,
    selectedItem: String
) {
    NavigationBarItem(
        selected = selectedItem == itemName,
        onClick = {
            onClick(itemName)
        },
        icon = {
            Icon(
                imageVector = imageVector,
                contentDescription = contentDescription,
                tint =
                    if (selectedItem == itemName) {
                        MaterialTheme.colorScheme.onSecondary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
            )
        },
        colors =
            NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.secondary,
                indicatorColor = MaterialTheme.colorScheme.secondary
            )
    )
}

sealed class BottomNavigationItem(val name: String) {
    data object Home : BottomNavigationItem("home")

    data object Group : BottomNavigationItem("group")

    data object Match : BottomNavigationItem("match")
}