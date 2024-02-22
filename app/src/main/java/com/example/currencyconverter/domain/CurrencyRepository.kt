package com.example.currencyconverter.domain

interface CurrencyRepository {
    suspend fun getCurrencies(): List<Currency>

}
