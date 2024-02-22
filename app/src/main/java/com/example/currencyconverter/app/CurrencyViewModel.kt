package com.example.currencyconverter.app

import android.util.Log
import androidx.datastore.dataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.data.DataStore.DataStoreManager
import com.example.currencyconverter.domain.Currency
import com.example.currencyconverter.domain.CurrencyRepository
import com.example.currencyconverter.utils.valute.ValuteNameOne
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

// ViewModel
@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : ViewModel() {

    private val _currencies = MutableLiveData<List<Currency>>()
    val currencies: LiveData<List<Currency>> get() = _currencies

    private val _actuliValuteOne = MutableLiveData<Currency?>()
    val ActuliValuteOne: LiveData<Currency?> get() = _actuliValuteOne

    private val _armenianCurrency = MutableLiveData<Currency?>()
    val armenianCurrency: LiveData<Currency?> get() = _armenianCurrency

    private val _usaCurrencyValue = MutableLiveData<Currency?>()
    val usaCurrency: LiveData<Currency?> get() = _usaCurrencyValue

    init {
        viewModelScope.launch {
            // Получаем все валюты
            val allCurrencies = currencyRepository.getCurrencies()

            // Присваиваем все валюты в _currencies
            _currencies.value = allCurrencies

            _actuliValuteOne.value = allCurrencies.find { it.Name.contains(ValuteNameOne) }
            Log.d("ХУУУУУЙ", ValuteNameOne)

            // Фильтруем и присваиваем армянскую валюту в _armenianCurrency
            _armenianCurrency.value = allCurrencies.find { it.Name.contains("Армянских драмов") }
            _usaCurrencyValue.value = allCurrencies.find { it.Name.contains("Доллар США") }



        }

    }
}

