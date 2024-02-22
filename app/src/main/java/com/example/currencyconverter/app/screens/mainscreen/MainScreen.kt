package com.example.currencyconverter.app.screens.mainscreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType

import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currencyconverter.R
import com.example.currencyconverter.app.CurrencyViewModel
import com.example.currencyconverter.data.DataStore.DataStoreManager
import com.example.currencyconverter.domain.Currency
import com.example.currencyconverter.utils.getFlagImageResource
import com.example.currencyconverter.utils.Country
import com.example.currencyconverter.utils.valute.ValuteNameOne
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlin.math.round

@OptIn(ExperimentalMaterial3Api::class)
@Composable
 fun Screen1(
    currencyViewModel: CurrencyViewModel = hiltViewModel()
 ) {
    // Получаем армянскую валюту внутри Composable
    val armenianCurrency by currencyViewModel.armenianCurrency.observeAsState()
    val usaCurrency by currencyViewModel.usaCurrency.observeAsState()
    val ActuliValuteOne by currencyViewModel.currencies.observeAsState()
    val ActuliValuteOneName by currencyViewModel.ActuliValuteOne.observeAsState()
   // val nameOne = ActuliValuteOneName
    val defaultValuteArmenia =armenianCurrency?.Previous
    val defaultValuteArmeniaName =armenianCurrency?.Name
    val defaultNominalArmenia =armenianCurrency?.Nominal
    val defaultValuteUsa =usaCurrency?.Previous
    val defaultNominalUsa =usaCurrency?.Nominal
    val defaultMameUsa =usaCurrency?.Name

    Log.d("DefaultValues", "Default Valute Armenia $defaultValuteArmenia")
    Log.d("DefaultValues", "Default Valute Armenia $defaultNominalArmenia")
    Log.d("DefaultValues", "Default Valute Armenia $defaultValuteUsa")
    Log.d("DefaultValues", "Default Valute Armenia $defaultNominalUsa")

    var selectedCurrency1 by remember { mutableStateOf<Currency?>(null) }
    var selectedCountry1 by remember { mutableStateOf(Country.ARMENIA) }
    var EditTextCountValueOne by rememberSaveable { mutableStateOf("") }

    var selectedCurrency2 by remember { mutableStateOf<Currency?>(null) }
    var selectedCountry2 by remember { mutableStateOf(Country.USA) }
    var EditTextCountValueTwo by rememberSaveable { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false) }
    var showDialogForPicture1 by remember { mutableStateOf(true) }

    val coroutine = rememberCoroutineScope()
    val context = LocalContext.current
    val dataStoreManager = DataStoreManager(context)

    var selectedCurrencyValue by remember { mutableStateOf(defaultValuteArmenia ?: 0.0)  }
    var selectedCurrencyValue1 by remember { mutableStateOf(defaultValuteUsa ?: 0.0) }


    var selectedCurrencyName by remember { mutableStateOf(defaultValuteArmeniaName ?: "")  }
    var selectedCurrencyName2 by remember { mutableStateOf(defaultMameUsa ?: "") }
    var convertedAmount: Double by remember { mutableStateOf(0.0) }

    val conversionRate1 by dataStoreManager.getConversionRate(true).collectAsState(initial = null)
    val conversionRate2 by dataStoreManager.getConversionRate(false).collectAsState(initial = null)

    // Переменные для номинала
    var selectedCurrencyNominal1 by remember { mutableStateOf(defaultNominalArmenia ?: 0) }
    var selectedCurrencyNominal2 by remember { mutableStateOf(defaultNominalUsa ?: 0) }

    var flagImageResource by remember(selectedCountry2) {
        mutableStateOf(selectedCountry2.getFlagImageResource())
    }

// Добавьте переменную для отслеживания текущего ресурса изображения
    var currentFlagImageResource by remember { mutableStateOf(flagImageResource) }

    LaunchedEffect(selectedCountry1) {
        dataStoreManager.getSelectedCountry(true).collect { country ->
            selectedCountry1 = country ?: Country.ARMENIA
            Log.d("DataStoreOne", "Loaded selectedCountry1: $country")

            // Получаем номинал из DataStore и сохраняем его в состоянии
            dataStoreManager.getCurrencyNominal(true).collect { nominal ->
                if (nominal != null) {
                    selectedCurrencyNominal1 = nominal

                } else
                {
                    if (defaultNominalArmenia != null) {
                        selectedCurrencyNominal1 = defaultNominalArmenia
                    }
                }

            }
        }
    }


// Загрузка данных из DataStore
    LaunchedEffect(selectedCountry2) {
        val country = dataStoreManager.getSelectedCountry(false).firstOrNull() ?: Country.USA
        selectedCountry2 = country
        Log.d("DataStoreTwo", "Loaded selectedCountry2: $country")

        val nominal = dataStoreManager.getCurrencyNominal(false).firstOrNull()
        if (nominal != null) {
            selectedCurrencyNominal2 = nominal
        } else
        {
            if (defaultNominalUsa != null) {
                selectedCurrencyNominal2 = defaultNominalUsa
            }
        }
        Log.d("DataStoreTwo", "Loaded selectedCurrencyNominal2: $nominal")

        // Обновление текущего изображения
        currentFlagImageResource = selectedCountry2.getFlagImageResource()
    }

    LaunchedEffect(selectedCurrency1) {
        dataStoreManager.getSelectedCurrency(true).collect { currency ->
            selectedCurrency1 = currency
            val valueOfEuro1 = selectedCurrency1?.Value ?: defaultValuteArmenia
            val valueOfEuro13 = selectedCurrency1?.Previous ?: defaultNominalArmenia
            val valueOfEuro133 = selectedCurrency1?.Name ?: defaultValuteUsa

            Log.d("valueOfEuro11111", valueOfEuro1.toString())
            Log.d("valueOfEuro112121", valueOfEuro13.toString())
            Log.d("valueOfEuro112121", valueOfEuro133.toString())
        }
    }

    LaunchedEffect(selectedCurrency2) {
        dataStoreManager.getSelectedCurrency(false).collectLatest { currency ->
            selectedCurrency2 = currency
            Log.d("valueOfEuro2", selectedCurrency2.toString())
        }
    }
    LaunchedEffect(Unit) {
        dataStoreManager.getSelectedCurrency(true).collect { currency ->
            selectedCurrency1 = currency
            val valueOfNameOne = selectedCurrency1?.Name ?: defaultValuteUsa
            ValuteNameOne = valueOfNameOne.toString()

        }
    }
    LaunchedEffect(selectedCurrency1, selectedCurrency2, EditTextCountValueOne) {
        val valueOfEuro1 = selectedCurrency1?.Value ?: defaultValuteArmenia
        val valueOfEuroNominal = selectedCurrency1?.Nominal ?: defaultNominalArmenia
        val valueOfEuro2 = selectedCurrency2?.Value ?: defaultValuteUsa
        val valueOfEuro2Nominal = selectedCurrency2?.Nominal ?: defaultNominalUsa
        Log.d("DDDD","$valueOfEuroNominal")
        Log.d("DDDD","$valueOfEuro2Nominal")

        val amount1Value = EditTextCountValueOne.toLongOrNull()

        if (valueOfEuro1 != null && valueOfEuro2 != null && amount1Value != null) {
            // If user changes the first input field, update the second input field
            val convertedAmount =
                (amount1Value * (valueOfEuro1 / valueOfEuroNominal!!) /
                        (valueOfEuro2 / valueOfEuro2Nominal!!) * 100) / 100
            val roundedConvertedAmount = round(convertedAmount * 10000) / 10000
            EditTextCountValueTwo = roundedConvertedAmount.toString()
        } else {
            println("Invalid input or null currency values")
        }
    }


    LaunchedEffect(selectedCurrency1, selectedCurrency2, EditTextCountValueTwo) {
        val valueOfEuro1 = selectedCurrency1?.Value ?: defaultValuteArmenia
        val valueOfEuroNominal = selectedCurrency1?.Nominal ?: defaultNominalArmenia
        val valueOfEuro2 = selectedCurrency2?.Value ?: defaultValuteUsa
        val valueOfEuro2Nominal = selectedCurrency2?.Nominal ?: defaultNominalUsa

        val amount2Value = EditTextCountValueTwo.toLongOrNull()

        if (valueOfEuro1 != null && valueOfEuro2 != null && amount2Value != null) {
            // Если пользователь изменяет второе поле ввода, обновите первое поле ввода
            val updatedAmount1 = round(
                (amount2Value * (valueOfEuro2 / valueOfEuro2Nominal!!)) / (valueOfEuro1 / valueOfEuroNominal!!) * 100
            ) / 100
            val formattedAmount = "%.4f".format(updatedAmount1)  // Форматирование с четырьмя знаками после запятой
            EditTextCountValueOne = formattedAmount
        } else {
            println("Invalid input or null currency values")
        }
    }

    // Increment the key to trigger recomposition

    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {
        Image(
            painter = painterResource(id = R.drawable.fon1),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .padding(20.dp)
                .padding(top = 20.dp)
                .height(250.dp) // Increased height to accommodate two rows
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    value = EditTextCountValueOne,
                    onValueChange = { newText ->
                        EditTextCountValueOne = newText
                    },
                    label = { Text("Валюта №1") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier
                        .weight(1f)

                )

                Box(
                    modifier = Modifier
                        .width(70.dp)
                        .height(70.dp)
                        .padding(start = 10.dp)
                        .padding(top = 5.dp)
                        .clickable {
                            showDialog = true
                            showDialogForPicture1 = true
                        }
                ) {
                    Image(
                        painter = painterResource(id = selectedCountry1.getFlagImageResource()),
                        contentDescription = "Выбрать валюту"
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    value = EditTextCountValueTwo,
                    onValueChange = { newText ->
                        EditTextCountValueTwo = newText
                    },
                    label = { Text("Валюта №2") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .width(70.dp)
                        .height(70.dp)
                        .padding(start = 10.dp, top = 5.dp)
                        .clickable {
                            showDialog = true
                            showDialogForPicture1 = false
                        }
                ) {
                    Image(
                        painter = painterResource(id = flagImageResource),
                        contentDescription = "Выбрать валюту"
                    )
                }
            }

            if (showDialog) {
                AlertDialogExample(
                    onDismissRequest = {
                        showDialog = false
                    },

                    onConfirmation = {
                        coroutine.launch {
                            if (showDialogForPicture1) {
                                selectedCurrency1?.let {

                                    dataStoreManager.saveSelectedCurrency(
                                        it,
                                        selectedCountry1,
                                        selectedCurrencyValue,
                                        selectedCurrencyNominal1,
                                        selectedCurrencyName,
                                        true
                                    )

                                }
                            } else {
                                selectedCurrency2?.let {
                                    dataStoreManager.saveSelectedCurrency(
                                        it,
                                        selectedCountry2,
                                        selectedCurrencyValue1,
                                        selectedCurrencyNominal2,
                                        selectedCurrencyName2,
                                        false
                                    )
                                }
                            }
                            showDialog = false
                        }
                    },

                    onCountrySelected = { newCountry, value, numinal, name ->
                        if (showDialogForPicture1) {
                            selectedCountry1 = newCountry
                            selectedCurrencyValue = value
                            selectedCurrencyNominal1 = numinal
                            selectedCurrencyName = name
                            coroutine.launch {
                                selectedCurrency1?.let {
                                   // val conversionRate1 = it.getConversionRate() // изменить переменную для сохранения
                                    dataStoreManager.saveSelectedCurrency(
                                        it,
                                        selectedCountry1,
                                        selectedCurrencyValue,
                                        selectedCurrencyNominal1,
                                        selectedCurrencyName,
                                        true
                                    )
                                    println("porno" + selectedCurrencyValue)
                                }
                            }

                        } else {
                            selectedCountry2 = newCountry
                            selectedCurrencyValue1 = value
                            selectedCurrencyNominal2 = numinal
                            selectedCurrencyName2 = name
                            coroutine.launch {
                                selectedCurrency2?.let {
                                    //val conversionRate2 = it.getConversionRate()
                                    dataStoreManager.saveSelectedCurrency(
                                        it,
                                        selectedCountry2,
                                        selectedCurrencyValue1,
                                        selectedCurrencyNominal2,
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

















