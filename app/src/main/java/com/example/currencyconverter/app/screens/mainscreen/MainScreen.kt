package com.example.currencyconverter.app.screens.mainscreen

import android.util.Log
import androidx.compose.foundation.Image

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currencyconverter.R
import com.example.currencyconverter.data.DataStore.DataStoreManager
import com.example.currencyconverter.domain.Currency
import com.example.currencyconverter.utils.getFlagImageResource
import com.example.currencyconverter.utils.Country
import com.example.currencyconverter.utils.valute.ValuteNameOne
import com.example.currencyconverter.utils.valute.ValuteNameTwo
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.currencyconverter.domain.models.CurrencyField
import com.example.currencyconverter.ui.theme.CurrencyConverterTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
@Composable
fun Screen1(
    currencyViewModel: CurrencyViewModel = hiltViewModel()
) {
    val armenianCurrency by currencyViewModel.armenianCurrency.observeAsState()
    val usaCurrency by currencyViewModel.usaCurrency.observeAsState()

    // Дефолтные значение(Армянская и Американская валюта)
    val defaultValuteArmeniaValue = armenianCurrency?.Value
    val defaultValuteArmeniaName = armenianCurrency?.Name
    val defaultNominalArmenia = armenianCurrency?.Nominal
    val defaultValuteUsa = usaCurrency?.Value
    val defaultNominalUsa = usaCurrency?.Nominal
    val defaultMameUsa = usaCurrency?.Name

    var selectedOneCurrency by remember { mutableStateOf<Currency?>(null) }
    var editTextCountValueFirst by rememberSaveable { mutableStateOf("") }
    var selectedTwoCurrency by remember { mutableStateOf<Currency?>(null) }
    var editTextCountValueSecond by rememberSaveable { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var showDialogForPicture by remember { mutableStateOf(true) }

    val coroutine = rememberCoroutineScope()
    val context = LocalContext.current
    val dataStoreManager = DataStoreManager(context)

    var selectedCurrencyValueFirst by remember {
        mutableStateOf(
            defaultValuteArmeniaValue ?: 0.0
        )
    }
    var selectedCurrencyValueSecond by remember { mutableStateOf(defaultValuteUsa ?: 0.0) }

    var selectedCurrencyName by remember { mutableStateOf(defaultValuteArmeniaName ?: "") }
    var selectedCurrencyName2 by remember { mutableStateOf(defaultMameUsa ?: "") }

    var selectedCurrencyNominalFirst by remember { mutableStateOf(defaultNominalArmenia ?: 0) }
    var selectedCurrencyNominalSecond by remember { mutableStateOf(defaultNominalUsa ?: 0) }
    // Загрузка сохраненных данных из DataStore
    val savedCurrencyFields by dataStoreManager.selectedCurrencies.collectAsState(initial = emptyList())

    // Сохранение и восстановление списка CurrencyField
    val currencyFieldSaver: Saver<MutableList<CurrencyField>, *> = listSaver(
        save = { list ->
            list.map { currencyField ->
                mapOf(
                    "value" to currencyField.value,
                    "currencyId" to (currencyField.selectedCurrency?.ID ?: ""),
                    "currencyName" to (currencyField.selectedCurrency?.Name ?: ""),
                    "country" to currencyField.selectedCountry.name
                )
            }
        },
        restore = { savedList ->
            savedList.map { map ->
                val value = map["value"] as String
                val currencyId = map["currencyId"] as String
                val currencyName = map["currencyName"] as String
                val countryName = map["country"] as String
                val selectedCountry = Country.valueOf(countryName)
                val selectedCurrency = Currency(
                    ID = currencyId,
                    NumCode = "",
                    CharCode = "",
                    Nominal = 1,
                    Name = currencyName,
                    Value = 0.0,
                    Previous = 0.0
                )
                CurrencyField(value, selectedCurrency, selectedCountry)
            }.toMutableList()
        }
    )

    var selectedFieldIndex by remember { mutableStateOf(0) }
    val selectedOneCountry by lazy {
        runBlocking {
            dataStoreManager.getSelectedCountry(true).first() ?: Country.ARMENIA
        }
    }

    val selectedTwoCountry by lazy {
        runBlocking {
            dataStoreManager.getSelectedCountry(false).first() ?: Country.USA
        }
    }

    val flagImageResource by remember(selectedTwoCountry) {
        mutableStateOf(selectedTwoCountry.getFlagImageResource())
    }

    // Переменная для отслеживания текущего ресурса изображения
    var currentFlagImageResource by remember { mutableStateOf(flagImageResource) }

    var isUsedSecond = true
    var isUsedFirst = true

    LaunchedEffect(selectedOneCountry) {
        dataStoreManager.getSelectedCountry(true).collect { _ ->
            dataStoreManager.getCurrencyNominal(true).collect { nominal ->
                if (nominal != null) {
                    selectedCurrencyNominalFirst = nominal
                } else {
                    if (defaultNominalArmenia != null) {
                        selectedCurrencyNominalFirst = defaultNominalArmenia
                    }
                }
            }
        }
    }

    // Загрузка данных из DataStore
    LaunchedEffect(selectedTwoCountry) {
        dataStoreManager.getSelectedCountry(false).firstOrNull() ?: Country.USA

        val nominal = dataStoreManager.getCurrencyNominal(false).firstOrNull()
        if (nominal != null) {
            selectedCurrencyNominalSecond = nominal
        } else {
            if (defaultNominalUsa != null) {
                selectedCurrencyNominalSecond = defaultNominalUsa
            }
        }
        currentFlagImageResource = selectedTwoCountry.getFlagImageResource()
    }

    LaunchedEffect(selectedOneCurrency) {
        dataStoreManager.getSelectedCurrency(true).collect { currency ->
            selectedOneCurrency = currency

            if (selectedOneCurrency?.Name!!.isNotEmpty()) {
                ValuteNameOne = selectedOneCurrency?.Name ?: ""
            }
            if (isUsedFirst && ValuteNameOne.isNotEmpty()) {
                currencyViewModel.updateValyte()
            }
            isUsedFirst = false
        }
    }

    LaunchedEffect(selectedTwoCurrency) {
        dataStoreManager.getSelectedCurrency(false).collectLatest { currency ->
            selectedTwoCurrency = currency
            if (selectedTwoCurrency?.Name!!.isNotEmpty()) {
                ValuteNameTwo = selectedTwoCurrency?.Name ?: ""
            }
            if (isUsedSecond && ValuteNameTwo.isNotEmpty()) {
                currencyViewModel.updateValyteTwo()
            }
            isUsedSecond = false
        }
    }

    LaunchedEffect(selectedOneCurrency, selectedTwoCurrency, editTextCountValueFirst) {
        val valueOfEuro1 = selectedOneCurrency?.Value ?: defaultValuteArmeniaValue
        val valueOfEuroNominal = selectedOneCurrency?.Nominal ?: defaultNominalArmenia
        val valueOfEuro2 = selectedTwoCurrency?.Value ?: defaultValuteUsa
        val valueOfEuro2Nominal = selectedTwoCurrency?.Nominal ?: defaultNominalUsa

        val amount1Value = editTextCountValueFirst.toLongOrNull()

        if (valueOfEuro1 != null && valueOfEuro2 != null && amount1Value != null) {
            val convertedAmount =
                (amount1Value * (valueOfEuro1 / valueOfEuroNominal!!) /
                        (valueOfEuro2 / valueOfEuro2Nominal!!))
            val formattedAmount = "%.4f".format(convertedAmount)
            editTextCountValueSecond = formattedAmount
        } else {
            println("Invalid input or null currency values")
        }
    }

    LaunchedEffect(selectedOneCurrency, selectedTwoCurrency, editTextCountValueSecond) {
        val valueOfEuro1 = selectedOneCurrency?.Value ?: defaultValuteArmeniaValue
        val valueOfEuroNominal = selectedOneCurrency?.Nominal ?: defaultNominalArmenia
        val valueOfEuro2 = selectedTwoCurrency?.Value ?: defaultValuteUsa
        val valueOfEuro2Nominal = selectedTwoCurrency?.Nominal ?: defaultNominalUsa

        val amount2Value = editTextCountValueSecond.toLongOrNull()

        if (valueOfEuro1 != null && valueOfEuro2 != null && amount2Value != null) {
            val updatedAmount1 =
                (amount2Value * (valueOfEuro2 / valueOfEuro2Nominal!!)) /
                        (valueOfEuro1 / valueOfEuroNominal!!)
            val formattedAmount = "%.4f".format(updatedAmount1)
            editTextCountValueFirst = formattedAmount
        } else {
            Log.d("LaunchedEffect","Invalid input or null currency values")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .padding(20.dp)
                .padding(top = 20.dp)
                .height(250.dp) // Increased height to accommodate two rows
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {
            CurrencyConverterTheme {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSystemInDarkTheme()) {
                            MaterialTheme.colorScheme.surface
                        } else {
                            MaterialTheme.colorScheme.onPrimary
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(align = Alignment.CenterVertically)
                        .height(100.dp),
                    shape = RoundedCornerShape(16.dp),

                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = editTextCountValueFirst,
                            onValueChange = { newText ->
                                editTextCountValueFirst = newText
                            },
                            label = { Text(stringResource(id = R.string.valute_one_editText)) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .align(Alignment.CenterVertically)
                                .padding(start = 10.dp),

                            )

                        Box(
                            modifier = Modifier
                                .width(70.dp)
                                .height(70.dp)
                                .padding(start = 10.dp, end = 5.dp)
                                .padding(top = 10.dp)
                                .clickable {
                                    showDialog = true
                                    showDialogForPicture = true
                                }
                        ) {
                            Image(
                                painter = painterResource(id = selectedOneCountry.getFlagImageResource()),
                                contentDescription = "Выбрать валюту"
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isSystemInDarkTheme()) {
                        MaterialTheme.colorScheme.surface
                    } else {
                        MaterialTheme.colorScheme.onPrimary
                    }
                ),
                modifier = Modifier
                    // Added bottom padding between cards
                    .then(Modifier.height(100.dp)),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )

            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = editTextCountValueSecond,
                        onValueChange = { newText ->
                            editTextCountValueSecond = newText
                        },
                        label = { Text(stringResource(id = R.string.valute_two_editText)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 10.dp) // Adjusted padding
                    )
                    Box(
                        modifier = Modifier
                            .width(70.dp)
                            .height(70.dp)
                            .padding(start = 10.dp, top = 10.dp, end = 5.dp)
                            .clickable {
                                showDialog = true
                                showDialogForPicture = false
                            }
                            .align(Alignment.CenterVertically)
                    ) {
                        Image(
                            painter = painterResource(id = flagImageResource),
                            contentDescription = "Выбрать валюту"
                        )
                    }
                }
            }

            if (showDialog) {
                AlertDialogExample(
                    onDismissRequest = {
                        showDialog = false
                    },

                    onConfirmation = {
                        coroutine.launch {
                            if (showDialogForPicture) {
                                selectedOneCurrency?.let {
                                    dataStoreManager.saveSelectedCurrency(
                                        it,
                                        selectedOneCountry,
                                        selectedCurrencyValueFirst,
                                        selectedCurrencyNominalFirst,
                                        selectedCurrencyName,
                                        true
                                    )
                                }
                            } else {
                                selectedTwoCurrency?.let {
                                    dataStoreManager.saveSelectedCurrency(
                                        it,
                                        selectedTwoCountry,
                                        selectedCurrencyValueSecond,
                                        selectedCurrencyNominalSecond,
                                        selectedCurrencyName2,
                                        false
                                    )
                                }
                            }
                            showDialog = false
                        }
                    },

                    onCountrySelected = { newCountry, value, numinal, name ->
                        if (showDialogForPicture) {
                            //selectedOneCountry = newCountry
                            selectedCurrencyValueFirst = value
                            selectedCurrencyNominalFirst = numinal
                            selectedCurrencyName = name
                            coroutine.launch {
                                selectedOneCurrency?.let {
                                    dataStoreManager.saveSelectedCurrency(
                                        it,
                                        newCountry,
                                        selectedCurrencyValueFirst,
                                        selectedCurrencyNominalFirst,
                                        selectedCurrencyName,
                                        true
                                    )
                                }
                            }

                        } else {
                            selectedCurrencyValueSecond = value
                            selectedCurrencyNominalSecond = numinal
                            selectedCurrencyName2 = name
                            coroutine.launch {
                                selectedTwoCurrency?.let {
                                    dataStoreManager.saveSelectedCurrency(
                                        it,
                                        newCountry,
                                        selectedCurrencyValueSecond,
                                        selectedCurrencyNominalSecond,
                                        selectedCurrencyName2,
                                        false
                                    )
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}