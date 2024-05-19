package com.example.currencyconverter

import android.os.Bundle
import android.provider.Settings.Global.getString
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.currencyconverter.app.screens.dialoge.AlertDialogueAddListCurrency
import com.example.currencyconverter.app.screens.mainscreen.CurrencyViewModel
import com.example.currencyconverter.botton_navigation.MainScreen
import com.example.currencyconverter.ui.theme.CurrencyConverterTheme
import com.example.currencyconverter.yandex.Banner
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: CurrencyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CurrencyConverterTheme {
                Scaffold(
                    topBar = {
                        MainScreenToolbar()
                    },
                    content = {
                        MainScreen()
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(it), // Ensure content is padded correctly within the Scaffold
                            verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier
                                .fillMaxWidth()
                                .height(30.dp))
                            Banner(id = R.string.banner_1)
                        }
                    }
                )
            }
        }

        viewModel.armenianCurrency.observe(this) {}
        viewModel.usaCurrency.observe(this) {}
        viewModel.currencies.observe(this) {}
    }
}

@Composable
fun MainScreenToolbar() {
    TopAppBar(
        title = { Text(stringResource(id = R.string.app_name)) },
        backgroundColor = MaterialTheme.colors.surface,
        actions = {
            IconButton(
                onClick = {
                    AlertDialogueAddListCurrency(
                        onDismissRequest = { /* Обработчик закрытия */ },
                        onConfirmation = { /* Обработчик подтверждения */ },
                        onCountrySelected = { country, value, nominal, name ->
                            // Обработка выбора валюты
                        },
                        currencyViewModel = viewModel // Передача viewModel
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }

        }
    )
}