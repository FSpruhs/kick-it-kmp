package com.spruhs.navigation

sealed class MainScreens(val route: String) {
    data object HomeScreen : MainScreens("Home")
    data object GroupScreen : MainScreens("Group")
    data object MatchScreen : MainScreens("Match")
    data object MessageScreen : MainScreens("Message")
    data object ProfileScreen : MainScreens("Profile")

}