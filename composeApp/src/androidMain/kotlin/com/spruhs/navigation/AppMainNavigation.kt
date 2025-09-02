package com.spruhs.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.spruhs.screens.main.BottomNavigationItem
import com.spruhs.screens.user.HomeScreen

@Composable
fun AppMainNavigation(
    navHostController: NavHostController,
    updateBottomNavigation: (BottomNavigationItem) -> Unit,
    onLogout: () -> Unit,
    setBackIcon: (Boolean) -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = MainScreens.HomeScreen.route
    ) {
        composable(MainScreens.HomeScreen.route) {
            HomeScreen()
        }
    }
}