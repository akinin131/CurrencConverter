package com.example.currencyconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.currencyconverter.app.screens.mainscreen.AlertDialogExample
import com.example.currencyconverter.app.screens.mainscreen.CurrencyViewModel
import com.example.currencyconverter.botton_navigation.MainScreen
import com.example.currencyconverter.ui.theme.CurrencyConverterTheme
import com.example.currencyconverter.utils.getFlagImageResource
import com.example.currencyconverter.yandex.Banner
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: CurrencyViewModel by viewModels()
    private var updateValute = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CurrencyConverterTheme() {
                Scaffold(
                    topBar = {
                        MainScreenToolbar()
                    },
                    content = {
                        MainScreen()
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(it),
                            verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp)
                            )
                            Banner(id = R.string.banner_1)
                        }
                    }
                )
            }
        }

        if (updateValute) {
            viewModel.updateCurrenciesIfNeeded()
            viewModel.updateSelectedCurrency()
            updateValute = false
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenToolbar(currencyViewModel: CurrencyViewModel = hiltViewModel()) {
    val currencyFields by currencyViewModel.getAllCurrencyFields()
        .collectAsState(initial = emptyList())
    val context = LocalContext.current
    val imageLoader = remember(context) { ImageLoader(context) }

    val imagePaintersMap = currencyFields.associateBy(
        { it.currencyId },
        { currencyField ->
            val imageRequest = ImageRequest.Builder(context)
                .data(currencyField.countryEnum.getFlagImageResource())
                .crossfade(true)
                .error(android.R.drawable.ic_delete)
                .build()
            rememberAsyncImagePainter(model = imageRequest, imageLoader = imageLoader)
        }
    )

    var showDialog by remember { mutableStateOf(false) }
    val isDarkTheme = isSystemInDarkTheme()
    val backgroundColor = if (isDarkTheme) {
        MaterialTheme.colorScheme.onSecondary
    } else {
        MaterialTheme.colorScheme.surface
    }
    TopAppBar(
        title = { Text(stringResource(id = R.string.app_name)) },
        modifier = Modifier.background(backgroundColor),
        actions = {
            IconButton(
                onClick = {
                    showDialog = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        }
    )

    if (showDialog) {
        AlertDialogExample(
            onDismissRequest = { showDialog = false },
            currencyFields = currencyFields,
            imagePaintersMap = imagePaintersMap,
            currencyViewModel = currencyViewModel,
            selectedCurrencyId = ""
        )
    }
}