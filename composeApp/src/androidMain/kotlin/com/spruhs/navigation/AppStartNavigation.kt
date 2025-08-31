package com.spruhs.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.spruhs.screens.auth.StartScreen

@Composable
fun AppStartNavigation(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = StartScreens.StartScreen.route,
    ) {
        composable(StartScreens.StartScreen.route) {
            StartScreen(
                onLoggedIn = { Log.i("StartScreen", "onLoggedIn") },
                onLoginFailed = { Log.i("StartScreen", "onLoginFailed") },
            )
        }
    }
}

sealed class StartScreens(val route: String) {
    data object StartScreen : StartScreens("Start")
}