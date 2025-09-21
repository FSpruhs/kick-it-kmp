package com.spruhs.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.spruhs.screens.group.CreateGroupScreen
import com.spruhs.screens.group.GroupScreen
import com.spruhs.screens.group.InvitePlayerScreen
import com.spruhs.screens.group.PlayerDetailsScreen
import com.spruhs.screens.main.BottomNavigationItem
import com.spruhs.screens.match.EnterResultScreen
import com.spruhs.screens.match.MatchResultDetailScreen
import com.spruhs.screens.match.MatchScreen
import com.spruhs.screens.match.PlanMatchScreen
import com.spruhs.screens.match.UpcomingMatchDetailsScreen
import com.spruhs.screens.user.HomeScreen
import com.spruhs.screens.user.ProfileScreen
import com.spruhs.screens.user.SelectGroupScreen

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
            setBackIcon(false)
            HomeScreen(
                onMatchClick = { matchId ->
                    navHostController.navigate("${MainScreens.MatchDetailScreen.route}/$matchId")
                }
            )
        }

        composable(MainScreens.ProfileScreen.route) {
            setBackIcon(true)
            ProfileScreen(onLogout)
        }
        composable(MainScreens.GroupScreen.route) {
            setBackIcon(false)
            GroupScreen(
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
            setBackIcon(true)
            CreateGroupScreen(
                onCreateGroupSuccess = { navHostController.navigate(MainScreens.HomeScreen.route) }
            )
        }

        composable(MainScreens.InvitePlayerScreen.route) {
            setBackIcon(true)
            InvitePlayerScreen(
                onPlayerInvitedSuccess = {
                    navHostController.navigate(MainScreens.GroupScreen.route)
                }
            )
        }

        composable(
            route = "${MainScreens.PlayerDetailsScreen.route}/{playerId}",
            arguments =
            listOf(
                navArgument("playerId") {
                    type = NavType.StringType
                }
            )
        ) {
            setBackIcon(true)
            PlayerDetailsScreen(
                playerId = it.arguments?.getString("playerId") ?: "",
                onLastMatchClick = { matchId ->
                    navHostController.navigate("${MainScreens.PlayerDetailsScreen.route}/$matchId")
                },
                onPlayerRemoved = {
                    navHostController.navigate(MainScreens.HomeScreen.route)
                    updateBottomNavigation(BottomNavigationItem.Home)
                }
            )
        }

        composable(
            route = "${MainScreens.EnterResultScreen.route}/{matchId}",
            arguments =
            listOf(
                navArgument("matchId") {
                    type = NavType.StringType
                }
            )
        ) {
            EnterResultScreen(
                matchId = it.arguments?.getString("matchId") ?: "",
                onResultEntered = {
                    navHostController.navigate(MainScreens.HomeScreen.route)
                    updateBottomNavigation(BottomNavigationItem.Home)
                }
            )
            setBackIcon(true)
        }

        composable(
            route = "${MainScreens.MatchResultDetailScreen.route}/{matchId}",
            arguments =
            listOf(
                navArgument("matchId") {
                    type = NavType.StringType
                }
            )
        ) {
            setBackIcon(true)
            MatchResultDetailScreen(
                matchId = it.arguments?.getString("matchId") ?: "",
                onEnterResultClick = { matchId ->
                    navHostController.navigate("${MainScreens.EnterResultScreen.route}/$matchId")
                },
                onBack = { navHostController.popBackStack() }
            )
        }

        composable(
            route = "${MainScreens.UpcomingMatchDetailScreen.route}/{matchId}",
            arguments =
            listOf(
                navArgument("matchId") {
                    type = NavType.StringType
                }
            )
        ) {
            setBackIcon(true)
            UpcomingMatchDetailsScreen(
                matchId = it.arguments?.getString("matchId") ?: "",
                onMatchCancelled = {
                    navHostController.navigate(MainScreens.HomeScreen.route)
                    updateBottomNavigation(BottomNavigationItem.Home)
                }
            )
        }

        composable(MainScreens.MatchScreen.route) {
            setBackIcon(false)
            MatchScreen(
                onPlanMatchClick = {
                    navHostController.navigate(MainScreens.PlanMatchScreen.route)
                },
                onUpcomingMatchClick = { matchId ->
                    navHostController.navigate(
                        "${MainScreens.UpcomingMatchDetailScreen.route}/$matchId"
                    )
                },
                onLastMatchClick = { matchId ->
                    navHostController.navigate(
                        "${MainScreens.MatchResultDetailScreen.route}/$matchId"
                    )
                }
            )
        }

        composable(MainScreens.PlanMatchScreen.route) {
            PlanMatchScreen(
                onPlanMatchSuccess = { navHostController.navigate(MainScreens.MatchScreen.route) }
            )
            setBackIcon(true)
        }

        composable(MainScreens.SelectGroupScreen.route) {
            setBackIcon(false)
            SelectGroupScreen()
        }
    }
}