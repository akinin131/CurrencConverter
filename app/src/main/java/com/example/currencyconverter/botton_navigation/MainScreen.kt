package com.example.currencyconverter.botton_navigation


import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.currencyconverter.app.CurrencyViewModel
import com.example.currencyconverter.app.SplashScreen
import kotlinx.coroutines.delay

@Composable
fun AppContent() {
    var splashScreenVisible by remember { mutableStateOf(true) }
    val viewModel: CurrencyViewModel = viewModel()
    viewModel.armenianCurrency
    viewModel.usaCurrency
    viewModel.currencies
    LaunchedEffect(true) {
        delay(2000)
        splashScreenVisible = false
    }

    if (splashScreenVisible) {
        SplashScreen()
    } else {
        MainScreen()
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen() {


    val navHostController = rememberNavController()
    Scaffold (
        bottomBar = { BottomNavigationFun(navController = navHostController) }
    ){
        NavGraph(navHostController = navHostController)
    }
}