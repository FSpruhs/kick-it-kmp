package com.spruhs.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.spruhs.screens.start.StartScreen

@Composable
fun AppStartNavigation(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = StartScreens.StartScreen.route,
    ) {
        composable(StartScreens.StartScreen.route) {
            StartScreen()
        }
    }
}

sealed class StartScreens(val route: String) {
    data object StartScreen : StartScreens("Start")
}