package com.spruhs.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.spruhs.screens.main.BottomNavigationItem
import com.spruhs.screens.user.HomeScreen
import com.spruhs.screens.user.ProfileScreen

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
            HomeScreen(
                onMatchClick = { matchId -> navHostController.navigate("${MainScreens.MatchDetailScreen.route}/$matchId") },
                setBackIcon = setBackIcon,
            )
        }

        composable(MainScreens.ProfileScreen.route) {
            ProfileScreen(onLogout)
        }
    }
}