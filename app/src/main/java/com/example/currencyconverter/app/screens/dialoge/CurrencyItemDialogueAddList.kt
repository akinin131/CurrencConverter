package com.example.currencyconverter.app.screens.dialoge

import android.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.currencyconverter.domain.Currency
import com.example.currencyconverter.utils.getFlagImageResource
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CurrencyItemDialogueAddList(
    currency: Currency,
    isSelected: Boolean,
    onCurrencySelected: (Currency, Int, Double, String) -> Unit
) {
    val context = LocalContext.current

    val imageLoader = ImageLoader.Builder(context)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .diskCachePolicy(CachePolicy.ENABLED)
        .build()
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        // Load the image with caching
        val imagePainter = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current)
                .data(data = currency.getCountry().getFlagImageResource())
                .apply(block = fun ImageRequest.Builder.() {
                    crossfade(true)
                    error(R.drawable.ic_delete)
                    memoryCachePolicy(CachePolicy.ENABLED)
                    diskCachePolicy(CachePolicy.ENABLED)
                }).build(), imageLoader = imageLoader
        )
        Image(
            painter = imagePainter,
            contentDescription = "Выбрать валюту",
            modifier = Modifier
                .size(55.dp)
                .padding(top = 5.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = currency.getNameValute(context),
                style = TextStyle(fontSize = 20.sp)
            )
            val formattedValue = NumberFormat.getNumberInstance(Locale.getDefault()).apply {
                maximumFractionDigits = 4
            }.format(currency.Nominal?.let { currency.Value?.div(it) })

            Text(
                text = formattedValue,
                style = TextStyle(fontSize = 16.sp)
            )
        }
    }
    if (!com.example.currencyconverter.app.screens.mainscreen.isOnline(context)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Нет интернета", fontSize = 20.sp)
        }

    }
}