package com.example.currencyconverter.app.screens.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.R
import com.example.currencyconverter.data.room.CurrencyFieldEntity
import com.example.currencyconverter.data.room.CurrencyRepositoryRoom
import com.example.currencyconverter.data.room.SelectedCurrency
import com.example.currencyconverter.domain.CurrencyRepository
import com.example.currencyconverter.utils.Country
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val repository: CurrencyRepository,
    private val repositoryRoom: CurrencyRepositoryRoom
) : ViewModel() {
    fun saveAllCurrency() {
        viewModelScope.launch {
            val serverCurrencies = repository.getCurrencies()
            val currencyEntities = serverCurrencies.map { currency ->
                CurrencyFieldEntity(
                    currencyId = currency.ID,
                    name = currency.Name,
                    countryEnum = currency.getCountry(),
                    country = currency.getNameValute(),
                    value = currency.Value,
                    nominal = currency.Nominal
                )
            }.toMutableList()
            currencyEntities.removeAll { it.currencyId == "R01589" }
            currencyEntities.add(
                CurrencyFieldEntity(
                    currencyId = "1",
                    name = "Рубль",
                    countryEnum = Country.RU,
                    country = R.string.valute_name_ruble,
                    value = 1.0,
                    nominal = 1
                )
            )
            repositoryRoom.saveCurrencyFields(currencyEntities)
        }
    }

    fun saveSelectedCurrency() {
        viewModelScope.launch {
            val serverCurrencies = repository.getCurrencies()
            val currencyEntities = serverCurrencies
                .filter { currency -> currency.ID == "R01060" || currency.ID == "R01530" }
                .map { currency ->
                    SelectedCurrency(
                        currencyId = currency.ID,
                        name = currency.Name,
                        countryEnum = currency.getCountry(),
                        country = currency.getNameValute(),
                        value = currency.Value,
                        nominal = currency.Nominal
                    )
                }

            repositoryRoom.saveSelectedCurrency(currencyEntities)
        }
    }

    private fun updateCurrencyField() {
        viewModelScope.launch {
            val serverCurrencies = repository.getCurrencies()
            val currencyEntities = serverCurrencies.map { currency ->
                CurrencyFieldEntity(
                    currencyId = currency.ID,
                    name = currency.Name,
                    countryEnum = currency.getCountry(),
                    country = currency.getNameValute(),
                    value = currency.Value,
                    nominal = currency.Nominal
                )
            }
            repositoryRoom.updateCurrencyField(currencyEntities)
        }
    }

    fun updateSelectedCurrency() {
        viewModelScope.launch {
            val serverCurrencies = repository.getCurrencies()
            val currencyEntities = serverCurrencies.map { currency ->
                SelectedCurrency(
                    currencyId = currency.ID,
                    name = currency.Name,
                    countryEnum = currency.getCountry(),
                    country = currency.getNameValute(),
                    value = currency.Value,
                    nominal = currency.Nominal
                )
            }
            repositoryRoom.updateSelectedCurrencyFieldList(currencyEntities)
        }
    }

    fun updateSelectedCurrencyId(
        selectedId: String,
        updatedCurrency: SelectedCurrency,
        shouldUpdate: Boolean = false
    ) {
        if (shouldUpdate) {
            viewModelScope.launch {
                try {
                    repositoryRoom.getSelectedCurrencyById(selectedId)
                        .collect { selectedCurrency ->
                            selectedCurrency?.let {
                                val newCurrency = SelectedCurrency(
                                    id = it.id,
                                    currencyId = updatedCurrency.currencyId,
                                    name = updatedCurrency.name,
                                    countryEnum = updatedCurrency.countryEnum,
                                    country = updatedCurrency.country,
                                    value = updatedCurrency.value,
                                    nominal = updatedCurrency.nominal
                                )
                                repositoryRoom.updateSelectedCurrencyField(newCurrency)
                                this.cancel()
                            }
                        }
                } catch (_: NumberFormatException) {
                }
            }
        }
    }

    fun updateCurrency(updatedCurrency: CurrencyFieldEntity) {
        viewModelScope.launch {
            repositoryRoom.updateCurrency(updatedCurrency)
        }
    }

    fun getAllCurrencyFields(): Flow<List<CurrencyFieldEntity>> {
        return repositoryRoom.getAllCurrencyFields()
    }

    fun saveSelectedCurrency(selectedCurrency: SelectedCurrency) {
        viewModelScope.launch {
            repositoryRoom.saveSelectedCurrency(selectedCurrency)
        }
    }

    fun getAllSelectedCurrencies(): Flow<List<SelectedCurrency?>> {
        return repositoryRoom.getAllSelectedCurrencies()
    }

    fun deleteSelectedCurrency(currency: SelectedCurrency?) {
        viewModelScope.launch {
            currency?.let { repositoryRoom.deleteSelectedCurrency(it) }
        }
    }

    fun updateCurrenciesIfNeeded() {
        viewModelScope.launch {
            val localCurrencies = repositoryRoom.getAllCurrencyFields().firstOrNull()
            val localSelectedCurrencies = repositoryRoom.getAllSelectedCurrencies().firstOrNull()
            saveAllCurrency()
            updateCurrencyField()
            if (localSelectedCurrencies.isNullOrEmpty()) {
                saveSelectedCurrency()
            }
        }
    }
}