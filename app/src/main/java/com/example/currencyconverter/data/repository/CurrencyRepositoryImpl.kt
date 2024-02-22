package com.example.currencyconverter.data.repository

import com.example.currencyconverter.data.network.CurrencyApiService
import com.example.currencyconverter.domain.Currency
import com.example.currencyconverter.domain.CurrencyRepository
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val apiService: CurrencyApiService // CurrencyApiService - ваш сервис Retrofit
) : CurrencyRepository {

    override suspend fun getCurrencies(): List<Currency> {
        return try {
            val response = apiService.getCurrencies()
            if (response.isSuccessful) {
                response.body()?.let { data ->
                    // Преобразуйте данные из API в список объектов Currency
                    data.Valute.values.map { valute ->
                        Currency(
                            ID = valute.ID,
                            NumCode = valute.NumCode,
                            CharCode = valute.CharCode,
                            Nominal = valute.Nominal,
                            Name = valute.Name,
                            Value = valute.Value,
                            Previous = valute.Previous
                        )
                    }
                } ?: emptyList()
            } else {
                // Обработка ошибки
                emptyList()
            }
        } catch (e: Exception) {
            // Обработка исключений
            emptyList()
        }
    }


}

