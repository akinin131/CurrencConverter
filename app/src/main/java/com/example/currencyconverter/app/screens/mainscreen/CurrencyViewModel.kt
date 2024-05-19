package com.example.currencyconverter.app.screens.mainscreen

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.data.DataStore.DataStoreManager
import com.example.currencyconverter.domain.Currency
import com.example.currencyconverter.domain.CurrencyRepository
import com.example.currencyconverter.utils.Country
import com.example.currencyconverter.utils.valute.ValuteNameOne
import com.example.currencyconverter.utils.valute.ValuteNameTwo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository,
    @ApplicationContext val context: Context
) : ViewModel() {

    private val _currencies = MutableLiveData<List<Currency>>()
    val currencies: LiveData<List<Currency>> get() = _currencies

    private val _actuliValuteOne = MutableLiveData<Currency?>()
    val actuliValuteOne: LiveData<Currency?> get() = _actuliValuteOne

    private val _actuliValuteTwo = MutableLiveData<Currency?>()
    val actuliValuteTwo: LiveData<Currency?> get() = _actuliValuteTwo

    private val _armenianCurrency = MutableLiveData<Currency?>()
    val armenianCurrency: LiveData<Currency?> get() = _armenianCurrency

    private val _usaCurrencyValue = MutableLiveData<Currency?>()
    val usaCurrency: LiveData<Currency?> get() = _usaCurrencyValue

    private val dataStoreManager = DataStoreManager(context)

    init {
        viewModelScope.launch {
            val allCurrencies = currencyRepository.getCurrencies()
            _currencies.value = allCurrencies
            _armenianCurrency.value = allCurrencies.find {
                it.Name.contains("Армянских драмов")
            }
            _usaCurrencyValue.value = allCurrencies.find {
                it.Name.contains("Доллар США")
            }
        }
    }

    fun updateValyte() {
        if (!ValuteNameOne.equals("Рубль")) {
            viewModelScope.launch {
                val allCurrencies = currencyRepository.getCurrencies()
                val selectedCurrency = allCurrencies.find { it.Name.contains(ValuteNameOne) }

                selectedCurrency?.let {
                    _actuliValuteOne.value = it
                    it.Value?.let { it1 ->
                        dataStoreManager.updateValueForSelectedCurrency(
                            it1,
                            true
                        )
                    }
                }
            }
        }
    }

    fun updateValyteTwo() {
        if (!ValuteNameTwo.equals("Рубль")) {
            viewModelScope.launch {
                val allCurrencies = currencyRepository.getCurrencies()
                val selectedCurrency = allCurrencies.find { it.Name.contains(ValuteNameTwo) }
                selectedCurrency?.let {
                    _actuliValuteOne.value = it
                    it.Value?.let { it1 ->
                        dataStoreManager.updateValueForSelectedCurrency(
                            it1,
                            false
                        )
                    }
                }
            }
        }
    }
    fun saveSelectedCurrency(country: Country, value: Double, nominal: Int, name: String) {
        // Вызываем метод updateValueForSelectedCurrency у dataStoreManager
        viewModelScope.launch {
            dataStoreManager.saveSelectedCurrencies(country, value, nominal, name, true) // Предположим, что для первой валюты
        }
    }
}