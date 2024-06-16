package com.example.currencyconverter.app.screens.mainscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currencyconverter.utils.getFlagImageResource
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen1(
    currencyViewModel: CurrencyViewModel = hiltViewModel()
) {
    val currencyFields by currencyViewModel.getAllCurrencyFields().collectAsState(initial = emptyList())
    val savedCurrencies by currencyViewModel.getAllSelectedCurrencies().collectAsState(initial = emptyList())
    var editTextCountValues by remember { mutableStateOf(List(savedCurrencies.size) { "" }) }
    val coroutineScope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var showDialogId by remember { mutableStateOf("") }
    val isDarkTheme = isSystemInDarkTheme()
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

    if (showDialog) {
        AlertDialogExample(
            onDismissRequest = { showDialog = false },
            currencyFields,
            imagePaintersMap,
            currencyViewModel = currencyViewModel,
            showDialogId,

        )
    }

    LaunchedEffect(savedCurrencies) {
        if (editTextCountValues.size != savedCurrencies.size) {
            editTextCountValues = List(savedCurrencies.size) { index ->
                editTextCountValues.getOrElse(index) { "" }
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            //.padding(top = 63.dp)
            .background(color = if (isDarkTheme) MaterialTheme.colorScheme.inverseOnSurface else Color.White)
            .padding(bottom = 60.dp),
    ) {
        itemsIndexed(savedCurrencies, key = { _, currency -> currency!!.id }) { index, currency ->
            val dismissState = rememberDismissState(
                confirmValueChange = { dismissValue ->
                    if (dismissValue == DismissValue.DismissedToEnd && savedCurrencies.size > 1) {
                        coroutineScope.launch {
                            currencyViewModel.deleteSelectedCurrency(currency)
                        }
                        true
                    } else false
                },
            )

            SwipeToDismiss(
                state = dismissState,
                directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
                background = {
                    DismissBackground(dismissState)
                },
                dismissContent = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 10.dp
                        ),
                        shape = RoundedCornerShape(4.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(if (isDarkTheme) MaterialTheme.colorScheme.onSecondary else Color.White) // Set background color based on theme
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                OutlinedTextField(
                                    value = editTextCountValues.getOrElse(index) { "" },
                                    onValueChange = { newValue ->
                                        val newValues = editTextCountValues.toMutableList()
                                        if (index < newValues.size) {
                                            newValues[index] = newValue
                                        }
                                        editTextCountValues = newValues

                                        val amount = newValue.toFloatOrNull() ?: 0f
                                        val valueOfInputCurrency = currency?.value ?: 1.0
                                        val nominalOfInputCurrency = currency?.nominal ?: 1

                                        val updatedValues = editTextCountValues.toMutableList()
                                        savedCurrencies.forEachIndexed { otherIndex, otherCurrency ->
                                            if (otherIndex != index) {
                                                val valueOfOutputCurrency =
                                                    otherCurrency?.value ?: 1.0
                                                val nominalOfOutputCurrency =
                                                    otherCurrency?.nominal ?: 1

                                                val convertedAmount = (
                                                        (amount * (valueOfInputCurrency / nominalOfInputCurrency)) / (valueOfOutputCurrency / nominalOfOutputCurrency) * 100
                                                        ) / 100

                                                if (otherIndex < updatedValues.size) {
                                                    updatedValues[otherIndex] =
                                                        "%.4f".format(convertedAmount)
                                                }
                                            }
                                        }
                                        editTextCountValues = updatedValues
                                    },
                                    label = {
                                        Text(currency?.country?.let { context.getString(it) } ?: "")
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Done
                                    ),
                                    modifier = Modifier
                                        .weight(0.8f)
                                )

                                Image(
                                    painter = painterResource(
                                        id = currency?.countryEnum?.getFlagImageResource() ?: 0
                                    ),
                                    contentDescription = currency?.name ?: "",
                                    modifier = Modifier
                                        .size(60.dp)
                                        .padding(start = 16.dp)
                                        .clickable {
                                            showDialog = true
                                            showDialogId = currency?.id.toString()!!
                                        }
                                )
                            }
                        }
                    }
                }
            )
        }
    }
}