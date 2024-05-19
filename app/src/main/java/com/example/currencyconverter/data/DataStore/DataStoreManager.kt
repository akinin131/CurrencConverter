package com.example.currencyconverter.data.DataStore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.currencyconverter.domain.Currency
import com.example.currencyconverter.domain.models.CurrencyField
import com.example.currencyconverter.utils.Country
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("data_store")

class DataStoreManager @Inject constructor(private val context: Context) {

    private val dataStoreKey1 = "selected_currency_key_1"
    private val dataStoreKey2 = "selected_currency_key_2"

    suspend fun saveSelectedCurrency(
        selectedCurrency: Currency,
        selectedCountry: Country,
        conversionValue: Double,
        nominal: Int,
        name: String,
        forPicture1: Boolean
    ) {
        val dataStoreKey = if (forPicture1) dataStoreKey1 else dataStoreKey2
        context.dataStore.edit { pref ->
            pref[stringPreferencesKey("${dataStoreKey}_id")] = selectedCurrency.ID
            pref[stringPreferencesKey("${dataStoreKey}_name")] = name
            pref[doublePreferencesKey("${dataStoreKey}_value")] = conversionValue
            pref[doublePreferencesKey("${dataStoreKey}_previous")] = selectedCurrency.Previous
            pref[intPreferencesKey("${dataStoreKey}_nominal")] = nominal // Сохраняем номинал
            pref[stringPreferencesKey("${dataStoreKey}_country")] = selectedCountry.name
        }
    }

    suspend fun updateValueForSelectedCurrency(
        newValue: Double,
        forPicture1: Boolean
    ) {
        val dataStoreKey = if (forPicture1) dataStoreKey1 else dataStoreKey2
        context.dataStore.edit { pref ->
            pref[doublePreferencesKey("${dataStoreKey}_value")] = newValue
        }
    }

    fun getCurrencyNominal(forPicture1: Boolean): Flow<Int?> =
        context.dataStore.data.map { preferences ->
            val dataStoreKey = if (forPicture1) dataStoreKey1 else dataStoreKey2
            preferences[intPreferencesKey("${dataStoreKey}_nominal")]
        }

    fun getSelectedCountry(forPicture1: Boolean): Flow<Country?> =
        context.dataStore.data.map { pref ->
            val dataStoreKey = if (forPicture1) dataStoreKey1 else dataStoreKey2
            val selectedCountryName = pref[stringPreferencesKey("${dataStoreKey}_country")]

            val country = selectedCountryName?.let { Country.valueOf(it) }
            Log.d("DataStore", "Loaded selectedCountry: $country, forPicture1: $forPicture1")

            return@map country
        }

    fun getSelectedCurrency(forPicture1: Boolean): Flow<Currency?> =
        context.dataStore.data.map { pref ->
            val dataStoreKey = if (forPicture1) dataStoreKey1 else dataStoreKey2
            val selectedCurrencyId = pref[stringPreferencesKey("${dataStoreKey}_id")]
            val selectedCurrencyName = pref[stringPreferencesKey("${dataStoreKey}_name")]
            val selectedCurrencyValue = pref[doublePreferencesKey("${dataStoreKey}_value")]
            val selectedCurrencyPrevious = pref[doublePreferencesKey("${dataStoreKey}_previous")]
            val selectedCurrencyNuminal = pref[intPreferencesKey("${dataStoreKey}_nominal")]

            val currency = Currency(
                ID = selectedCurrencyId ?: "",
                NumCode = "",
                CharCode = "",
                Nominal = selectedCurrencyNuminal,
                Name = selectedCurrencyName ?: "",
                Value = selectedCurrencyValue,
                Previous = selectedCurrencyPrevious ?: 0.0
            )
            Log.d("DataStore", "Loaded selectedCurrency: $currency, forPicture1: $forPicture1")

            return@map currency
        }

    companion object {
        val SELECTED_CURRENCIES_KEY = stringPreferencesKey("selected_currencies_key")
    }

    suspend fun saveSelectedCurrencies(currencies: List<CurrencyField>) {
        context.dataStore.edit { preferences ->
            currencies.forEachIndexed { index, currencyField ->
                val keyPrefix = "currency_$index"
                preferences[stringPreferencesKey("${keyPrefix}_id")] =
                    currencyField.selectedCurrency?.ID ?: ""
                preferences[stringPreferencesKey("${keyPrefix}_name")] =
                    currencyField.selectedCurrency?.Name ?: ""
                preferences[stringPreferencesKey("${keyPrefix}_country")] =
                    currencyField.selectedCountry.name
                preferences[stringPreferencesKey("${keyPrefix}_value")] = currencyField.value
            }
            preferences[intPreferencesKey("currency_count")] = currencies.size
        }
    }


    val selectedCurrencies: Flow<List<CurrencyField>> = context.dataStore.data
        .map { preferences ->
            val count = preferences[intPreferencesKey("currency_count")] ?: 0
            val currencies = mutableListOf<CurrencyField>()
            for (i in 0 until count) {
                val keyPrefix = "currency_$i"
                val id = preferences[stringPreferencesKey("${keyPrefix}_id")] ?: ""
                val name = preferences[stringPreferencesKey("${keyPrefix}_name")] ?: ""
                val countryName = preferences[stringPreferencesKey("${keyPrefix}_country")] ?: ""
                val value = preferences[stringPreferencesKey("${keyPrefix}_value")] ?: ""
                val country = Country.valueOf(countryName)
                val currency = Currency(
                    ID = id,
                    NumCode = "",
                    CharCode = "",
                    Nominal = 1,
                    Name = name,
                    Value = 0.0,
                    Previous = 0.0
                )
                currencies.add(CurrencyField(value, currency, country))
            }
            currencies
        }
}
