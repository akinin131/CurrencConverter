package com.example.currencyconverter.app.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun Screen2() {
    Text(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight(),
        text = "В разработке",
        textAlign = TextAlign.Center
    )
}
