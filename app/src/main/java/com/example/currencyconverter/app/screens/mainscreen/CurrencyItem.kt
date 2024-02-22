package com.example.currencyconverter.app.screens.mainscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.currencyconverter.domain.Currency

@Composable
fun CurrencyItem(
    currency: Currency,
    isSelected: Boolean,
    onCurrencySelected: (Currency, Int, Double, String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        RadioButton(
            selected = isSelected,
            onClick = {
                currency.Nominal?.let {
                    currency.Value?.let { it1 ->
                        currency.Name?.let { name ->
                            onCurrencySelected(currency, it, it1, name)
                        }
                    }
                }
            }
        )

        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = currency.Name,
                style = TextStyle(fontSize = 20.sp)
            )
            Text(
                text = "${currency.Value}",
                style = TextStyle(fontSize = 16.sp)
            )
        }
    }
}