package com.example.currencyconverter.domain

import com.example.currencyconverter.utils.Country

data class Currency(
    val ID: String,
    val NumCode: String,
    val CharCode: String,
    val Nominal: Int?,
    val Name: String,
    val Value: Double?,
    val Previous: Double
) {

    fun getCountry(): Country {
        return when {
            Name.contains("Доллар") -> {Country.USA}
            Name.contains("Евро") -> Country.EURO
            Name.contains("Армянских драмов") -> Country.ARMENIA
            Name.contains("Грузинский лари") -> Country.GEORGIA
            Name.contains("Рубль") -> Country.RU

            else -> Country.USA
        }
    }

    fun getConversionRate(): Double {
        return Previous
    }
}
