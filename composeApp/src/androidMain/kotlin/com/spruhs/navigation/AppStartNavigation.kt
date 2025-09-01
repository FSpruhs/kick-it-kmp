package com.spruhs.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.spruhs.screens.start.LoginScreen
import com.spruhs.screens.start.StartScreen

@Composable
fun AppStartNavigation(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = StartScreens.StartScreen.route
    ) {
        composable(StartScreens.StartScreen.route) {
            StartScreen(
                onLoggedIn = { Log.i("StartScreen", "onLoggedIn") },
                onLoginFailed = {
                    navHostController.navigate(StartScreens.LoginScreen.route)
                }
            )
        }

        composable(StartScreens.LoginScreen.route) {
            LoginScreen(
                onLoggedIn = { Log.i("LoginScreen", "onLoggedIn") },
                onRegisterClick = { Log.i("LoginScreen", "onRegisterClick") }
            )
        }
    }
}

sealed class StartScreens(val route: String) {
    data object StartScreen : StartScreens("Start")
    data object LoginScreen : StartScreens("Login")
}