package com.example.currencyconverter.botton_navigation


import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.currencyconverter.ui.theme.CurrencyConverterTheme

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val navHostController = rememberNavController()
    Scaffold (
        //bottomBar = { BottomNavigationFun(navController = navHostController) }
    ){
        CurrencyConverterTheme {
            NavGraph(navHostController = navHostController)
        }
    }
}
