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
        nominal: Int, // Добавляем номинал
        name: String, // Добавляем номинал
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

    suspend fun updateSelectedCurrency(
        //selectedCurrency: Currency,
        conversionValue: Double,
        nominal: Int,
        forPicture1: Boolean
    ) {
        val dataStoreKey = if (forPicture1) dataStoreKey1 else dataStoreKey2
        context.dataStore.edit { pref ->
            pref[doublePreferencesKey("${dataStoreKey}_value")] = conversionValue
            pref[intPreferencesKey("${dataStoreKey}_nominal")] = nominal
        }
    }
    fun getSelectedCurrencyName(forPicture1: Boolean): Flow<String?> =
        context.dataStore.data.map { preferences ->
            val dataStoreKey = if (forPicture1) dataStoreKey1 else dataStoreKey2
            preferences[stringPreferencesKey("${dataStoreKey}_name")]
        }
    // Функция для обновления выбранной валюты в DataStore

    fun getCurrencyNominal(forPicture1: Boolean): Flow<Int?> =
        context.dataStore.data.map { preferences ->
            val dataStoreKey = if (forPicture1) dataStoreKey1 else dataStoreKey2
            preferences[intPreferencesKey("${dataStoreKey}_nominal")]
        }


    fun getConversionRate(forPicture1: Boolean): Flow<Double?> =
        context.dataStore.data.map { preferences ->
            val dataStoreKey = if (forPicture1) dataStoreKey1 else dataStoreKey2
            preferences[doublePreferencesKey("${dataStoreKey}_conversionRate")]
        }


    fun getSelectedCountry(forPicture1: Boolean): Flow<Country?> = context.dataStore.data.map { pref ->
        val dataStoreKey = if (forPicture1) dataStoreKey1 else dataStoreKey2
        val selectedCountryName = pref[stringPreferencesKey("${dataStoreKey}_country")]

        val country = selectedCountryName?.let { Country.valueOf(it) }
        // Добавьте лог для проверки
        Log.d("DataStore", "Loaded selectedCountry: $country, forPicture1: $forPicture1")

        return@map country
    }

    fun getSelectedCurrency(forPicture1: Boolean): Flow<Currency?> = context.dataStore.data.map { pref ->
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

        // Добавьте лог для проверки
        Log.d("DataStore", "Loaded selectedCurrency: $currency, forPicture1: $forPicture1")

        return@map currency
    }



}






