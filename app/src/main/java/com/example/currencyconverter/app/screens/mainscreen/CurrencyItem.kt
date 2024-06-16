package com.example.currencyconverter.app.screens.mainscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import com.example.currencyconverter.data.room.CurrencyFieldEntity
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CurrencyItem(
    currency: CurrencyFieldEntity,
    isSelected: Boolean,
    onCurrencySelected: (CurrencyFieldEntity) -> Unit,
    onToggleFavorite: (CurrencyFieldEntity) -> Unit,
    imagePainter: AsyncImagePainter
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Image(
            painter = imagePainter,
            contentDescription = "Выбрать валюту",
            modifier = Modifier
                .size(55.dp)
                .padding(top = 5.dp)
        )

        RadioButton(
            selected = isSelected,
            onClick = { onCurrencySelected(currency) }
        )

        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(currency.country),
                style = TextStyle(fontSize = 20.sp)
            )

            Text(
                text = NumberFormat.getNumberInstance(Locale.getDefault())
                    .apply {
                        maximumFractionDigits = 4
                    }.format(currency.nominal?.let { currency.value?.div(it) } ?: 0.0),
                style = TextStyle(fontSize = 16.sp)
            )
        }

        IconButton(
            onClick = { onToggleFavorite(currency) },
            modifier = Modifier
                .align(Alignment.CenterVertically)
        ) {
            Icon(
                imageVector = if (currency.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = if (currency.isFavorite) "Удалить из избранного" else "Добавить в избранное",
                tint = if (currency.isFavorite) Color.Red else Color.Gray)
        }
    }
}