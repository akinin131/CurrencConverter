package com.example.currencyconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.currencyconverter.app.CurrencyViewModel
import com.example.currencyconverter.botton_navigation.AppContent
import com.example.currencyconverter.botton_navigation.MainScreen
import com.example.currencyconverter.ui.theme.CurrencyConverterTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: CurrencyViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            CurrencyConverterTheme {

                MainScreen()

            }
        }
        viewModel.armenianCurrency.observe(this) { armenianCurrency ->
            // Do something with armenianCurrency
        }

        viewModel.usaCurrency.observe(this) { usaCurrency ->
            // Do something with usaCurrency
        }

        viewModel.currencies.observe(this) { currencies ->
            // Do something with currencies
        }
    }
}


