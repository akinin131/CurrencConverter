package com.example.currencyconverter.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.currencyconverter.R

@Composable
fun SplashScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash),
            contentDescription = null,
            modifier = Modifier
                .size(220.dp)
                .padding(16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
          Text(
            text = "Конвертер валют",
           // style = MaterialTheme.typography.h4,
            color = MaterialTheme.colorScheme.primary
        )
    }
}