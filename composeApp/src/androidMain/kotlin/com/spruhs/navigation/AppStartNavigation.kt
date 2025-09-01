package com.spruhs.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.spruhs.screens.start.LoginScreen
import com.spruhs.screens.start.RegisterScreen
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
                onRegisterClick = { navHostController.navigate(StartScreens.RegisterScreen.route) }
            )
        }

        composable(StartScreens.RegisterScreen.route) {
            RegisterScreen(
                onBackClick = { navHostController.popBackStack() },
                onRegisterSuccess = { Log.i("RegisterScreen", "onRegisterSuccess") }
            )
        }
    }
}