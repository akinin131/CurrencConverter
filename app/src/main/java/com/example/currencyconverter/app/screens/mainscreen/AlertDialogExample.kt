package com.example.currencyconverter.app.screens.mainscreen

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currencyconverter.app.CurrencyViewModel
import com.example.currencyconverter.domain.Currency
import com.example.currencyconverter.utils.Country
import com.example.currencyconverter.utils.valute.ValuteNameOne

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
    // Use the currencies observed from the view model
    val currencies by currencyViewModel.currencies.observeAsState(emptyList())

    // Combine the static currencies with the observed currencies
    val allCurrencies = staticCurrencies + currencies

    // Filter the combined list
    val filteredCurrencies = allCurrencies.filter { currency ->
        currency.Name in setOf("Армянских драмов", "Доллар США", "Грузинский лари", "Евро","Рубль")
    }

    // State to track the selected currency
    var selectedCurrency by remember { mutableStateOf<Currency?>(null) }

    // State to track whether at least one RadioButton is selected
    val isAtLeastOneSelected = selectedCurrency != null

    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(
                onClick = {
                    if (isAtLeastOneSelected) {
                        selectedCurrency?.let { currency ->
                            onCountrySelected(
                                currency.getCountry(),
                                currency.Value ?: 0.0,
                                currency.Nominal ?: 0,
                                currency.Name ?: ""
                            )
                            onDismissRequest()
                        }
                    } else {
                        // Если ни один RadioButton не выбран, просто закрываем диалоговое окно
                        onDismissRequest()
                    }
                },
                enabled = isAtLeastOneSelected // Делаем кнопку "Ок" неактивной, если ни один RadioButton не выбран
            ) {
                Text("Ок")
            }
        },

        title = {
            LazyColumn(
                modifier = Modifier.height(450.dp)
            ) {
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
        }
    )
}
