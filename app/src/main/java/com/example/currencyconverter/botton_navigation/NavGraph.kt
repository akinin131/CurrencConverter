package com.example.currencyconverter.botton_navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.currencyconverter.app.screens.mainscreen.Screen1
import com.example.currencyconverter.app.screens.Screen2

@Composable
fun NavGraph(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = "screen_1"
    ) {
        composable("screen_1") {
            Screen1()
        }
        composable("screen_2") {
            Screen2()
        }
    }
}
