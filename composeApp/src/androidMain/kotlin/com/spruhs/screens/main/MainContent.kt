package com.spruhs.screens.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.spruhs.navigation.AppMainNavigation
import com.spruhs.navigation.MainScreens

@Composable
fun MainContent(onLogout: () -> Unit) {
    var backIcon by remember { mutableStateOf(false) }
    val items = listOf("home", "group", "match")
    val selectedItem = remember { mutableStateOf(items[0]) }
    val navHostController = rememberNavController()

    Scaffold(
        topBar = {
            KickItTopBar(
                backIcon,
                onBackClick = { navHostController.popBackStack() },
                onMessageClick = { navHostController.navigate(MainScreens.MessageScreen.route) },
                onProfileClick = { navHostController.navigate(MainScreens.ProfileScreen.route) }
            )
        },
        content = { paddingValues ->
            Column(
                modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                AppMainNavigation(
                    navHostController = navHostController,
                    updateBottomNavigation = {
                        selectedItem.value = it.name
                    },
                    onLogout = { onLogout() }
                ) { value ->
                    backIcon = value
                }
            }
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = selectedItem.value,
                onHomeClick = {
                    navHostController.navigate(MainScreens.HomeScreen.route)
                    selectedItem.value = BottomNavigationItem.Home.name
                    backIcon = false
                },
                onGroupClick = {
                    navHostController.navigate(MainScreens.GroupScreen.route)
                    selectedItem.value = BottomNavigationItem.Group.name
                    backIcon = false
                },
                onMatchClick = {
                    navHostController.navigate(MainScreens.MatchScreen.route)
                    selectedItem.value = BottomNavigationItem.Match.name
                    backIcon = false
                }
            )
        }
    )
}