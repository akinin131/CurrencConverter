package com.example.currencyconverter.domain.models

import com.example.currencyconverter.domain.Currency
import com.example.currencyconverter.utils.Country

data class CurrencyField(
    var value: String,
    var selectedCurrency: Currency? = null,
    var selectedCountry: Country = Country.ARMENIA
)
