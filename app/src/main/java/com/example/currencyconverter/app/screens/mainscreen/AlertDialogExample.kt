package com.example.currencyconverter.app.screens.mainscreen

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import com.example.currencyconverter.data.room.CurrencyFieldEntity
import com.example.currencyconverter.data.room.SelectedCurrency

@Composable
fun SearchBarWithBackButton(
    query: String,
    onQueryChange: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDarkTheme = isSystemInDarkTheme()

    val backgroundColor = if (isDarkTheme) {
        Color.Black
    } else {
        MaterialTheme.colorScheme.surface
    }

    val borderColor = if (isDarkTheme) {
        Color.Gray
    } else {
        MaterialTheme.colorScheme.onSurface.copy(0.5f)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(1.dp)

    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = LocalContentColor.current
            )
        }
        TextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text(text = "Search", color = LocalContentColor.current) },
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor, RoundedCornerShape(16.dp))
                .border(1.dp, borderColor, RoundedCornerShape(16.dp)),
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = LocalContentColor.current
                )
            },
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                textColor = LocalContentColor.current,
                cursorColor = LocalContentColor.current,
                leadingIconColor = LocalContentColor.current,
                placeholderColor = LocalContentColor.current,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
    }
}

@Composable
fun AlertDialogExample(
    onDismissRequest: () -> Unit,
    currencyFields: List<CurrencyFieldEntity>,
    imagePaintersMap: Map<String, AsyncImagePainter>,
    currencyViewModel: CurrencyViewModel = hiltViewModel(),
    selectedCurrencyId: String
) {
    val context = LocalContext.current

    val (selectedCurrency, setSelectedCurrency) = remember { mutableStateOf<CurrencyFieldEntity?>(null) }
    val (searchQuery, setSearchQuery) = remember { mutableStateOf("") }

    val filteredCurrencyFields = currencyFields.filter { currencyField ->
        currencyField.countryEnum.name.contains(searchQuery, ignoreCase = true)
    }

    val sortedFilteredCurrencyFields = filteredCurrencyFields.sortedByDescending { it.isFavorite }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .align(Alignment.Center),
                shape = MaterialTheme.shapes.medium,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    SearchBarWithBackButton(
                        query = searchQuery,
                        onQueryChange = setSearchQuery,
                        onBackClick = onDismissRequest
                    )
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        itemsIndexed(sortedFilteredCurrencyFields) { index, currencyField ->
                            CurrencyItem(
                                currency = currencyField,
                                isSelected = selectedCurrency == currencyField,
                                onCurrencySelected = {
                                    setSelectedCurrency(it)
                                },
                                onToggleFavorite = {
                                    val updatedCurrency =
                                        currencyField.copy(isFavorite = !currencyField.isFavorite)
                                    currencyViewModel.updateCurrency(updatedCurrency)
                                },
                                imagePainter = imagePaintersMap[currencyField.currencyId]!!
                            )
                            Divider()
                        }
                    }
                    Button(
                        onClick = {
                            selectedCurrency?.let {
                                val selectedCurrencyEntity = SelectedCurrency(
                                    currencyId = it.currencyId,
                                    name = it.name,
                                    countryEnum = it.countryEnum, // Передача значения из поля countryEnum
                                    country = it.country,
                                    value = it.value ?: 0.0,
                                    nominal = it.nominal ?: 0
                                )
                                if (selectedCurrencyId == "") {
                                    currencyViewModel.saveSelectedCurrency(selectedCurrencyEntity)
                                } else {
                                    currencyViewModel.updateSelectedCurrencyId(selectedCurrencyId, selectedCurrencyEntity, true)
                                }
                            }
                            onDismissRequest()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Text(text = "Ok")
                    }
                }
            }
        }
    }
}

fun isOnline(context: Context): Boolean {
  val connectivityManager =
      context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
  val network = connectivityManager.activeNetwork
  val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
  return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
}