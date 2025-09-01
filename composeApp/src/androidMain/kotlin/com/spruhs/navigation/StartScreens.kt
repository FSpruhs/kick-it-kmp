package com.spruhs.navigation

sealed class StartScreens(val route: String) {
    data object StartScreen : StartScreens("Start")
    data object LoginScreen : StartScreens("Login")
    data object RegisterScreen : StartScreens("Register")
}