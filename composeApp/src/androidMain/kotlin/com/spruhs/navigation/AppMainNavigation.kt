package com.spruhs.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.spruhs.screens.group.CreateGroupScreen
import com.spruhs.screens.group.GroupScreen
import com.spruhs.screens.group.InvitePlayerScreen
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
                onMatchClick = { matchId ->
                    navHostController.navigate("${MainScreens.MatchDetailScreen.route}/$matchId")
                },
                setBackIcon = setBackIcon
            )
        }

        composable(MainScreens.ProfileScreen.route) {
            ProfileScreen(onLogout)
        }
        composable(MainScreens.GroupScreen.route) {
            GroupScreen(
                setBackIcon = setBackIcon,
                onInvitePlayerClick = {
                    navHostController.navigate(MainScreens.InvitePlayerScreen.route)
                },
                onPlayerClick = { playerId ->
                    navHostController.navigate("${MainScreens.PlayerDetailsScreen.route}/$playerId")
                },
                onLeaveGroup = {
                    navHostController.navigate(MainScreens.HomeScreen.route)
                    updateBottomNavigation(BottomNavigationItem.Home)
                }
            )
        }
        composable(MainScreens.CreateGroupScreen.route) {
            CreateGroupScreen(
                onCreateGroupSuccess = { navHostController.navigate(MainScreens.HomeScreen.route) }
            )
        }

        composable(MainScreens.InvitePlayerScreen.route) {
            InvitePlayerScreen(
                onPlayerInvitedSuccess = { navHostController.navigate(MainScreens.GroupScreen.route) }
            )
        }
    }
}