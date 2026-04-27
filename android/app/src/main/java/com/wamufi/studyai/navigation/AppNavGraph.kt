package com.wamufi.studyai.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.wamufi.studyai.MainScreen
import com.wamufi.studyai.ui.ChatScreen
import com.wamufi.studyai.ui.TextSummaryScreen

sealed class Screen(val route: String, val title: String) {
    object Main : Screen("main", "Main")
    object ChatScreen : Screen("ChatScreen", "Chat Screen")
    object TextSummaryScreen : Screen("TextSummaryScreen", "Text Summary Screen")
}

val MAIN_SCREENS = listOf(
    Screen.ChatScreen,
    Screen.TextSummaryScreen
)

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Main.route) {
        composable(Screen.Main.route) {
            MainScreen(navController)
        }

        composable(Screen.ChatScreen.route) {
            ChatScreen(navController)
        }

        composable(Screen.TextSummaryScreen.route) {
            TextSummaryScreen(navController)
        }
    }
}