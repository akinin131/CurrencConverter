package com.example.currencyconverter.app.screens.mainscreen

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currencyconverter.R
import com.example.currencyconverter.domain.Currency
import com.example.currencyconverter.utils.Country

val staticCurrencies = listOf(
    Currency("1", "Рубль", "RU", 1, "Рубль", 1.0, 1.0),
)

@Composable
fun AlertDialogExample(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    onCountrySelected: (Country, Double, Int, String) -> Unit,
    currencyViewModel: CurrencyViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val currencies by currencyViewModel.currencies.observeAsState(emptyList())
    val allCurrencies = if (isOnline(context) && currencies.isNotEmpty()) {
        staticCurrencies + currencies
    } else {
        currencies
    }
    val preferredCurrencies = allCurrencies.filter { currency ->
        currency.Name in setOf("Армянских драмов", "Доллар США", "Грузинский лари", "Евро", "Рубль")
    }
    val otherCurrencies = allCurrencies - preferredCurrencies.toSet()
    var sortedCurrencies = preferredCurrencies + otherCurrencies
    var selectedCurrency by remember { mutableStateOf<Currency?>(null) }

    val isAtLeastOneSelected = selectedCurrency != null

    var searchQuery by remember { mutableStateOf("") }
    sortedCurrencies = sortedCurrencies.filter { currency ->
        currency.Name != "СДР (специальные права заимствования)"
    }

    BackHandler(enabled = true, onBack = {
        onDismissRequest()
    })

    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(usePlatformDefaultWidth = false),
        content = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Search bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text(text = context.getString(R.string.search)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        leadingIcon = {
                            IconButton(
                                onClick = { onDismissRequest() }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    )
                    if (!isOnline(context) || currencies.isEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id =R.drawable.wifi),
                                contentDescription = "Выбрать валюту",
                                modifier = Modifier
                                    .size(55.dp)
                                    .padding(top = 5.dp)
                            )
                            Text(text = context.resources.getString(R.string.not_connection), fontSize = 24.sp)
                            Text(text = context.resources.getString(R.string.check_connection), fontSize = 16.sp)
                        }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        val filteredCurrencies = sortedCurrencies.filter {
                            it.getNameValute(context).contains(searchQuery, ignoreCase = true)
                        }
                        items(filteredCurrencies) { currency ->
                            CurrencyItem(
                                currency = currency,
                                isSelected = currency.Name == selectedCurrency?.Name,
                                onCurrencySelected = { selected, nominal, value, name ->
                                    selectedCurrency = selected
                                    onCountrySelected(selected.getCountry(), value, nominal, name)
                                }
                            )
                        }
                    }
                    Button(
                        onClick = {
                            if (isAtLeastOneSelected) {
                                selectedCurrency?.let { currency ->
                                    onCountrySelected(
                                        currency.getCountry(),
                                        currency.Value ?: 0.0,
                                        currency.Nominal ?: 0,
                                        currency.Name
                                    )
                                    onConfirmation()
                                }
                            } else {
                                onDismissRequest()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    ) {
                        Text("Ок")
                    }
                }
            }
        }
    )
}

fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork
    val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
    return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
}