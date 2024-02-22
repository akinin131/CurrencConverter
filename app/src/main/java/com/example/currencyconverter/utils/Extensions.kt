package com.example.currencyconverter.utils

import com.example.currencyconverter.R

fun Country.getFlagImageResource(): Int {
    return when (this) {
        Country.USA -> R.drawable.usa_flag
        Country.RUSSIA -> R.drawable.usa_flag
        Country.ARMENIA -> R.drawable.flag_armenia
        Country.EURO -> R.drawable.flag_euro
        Country.GEORGIA -> R.drawable.flag_georgia
        Country.RU -> R.drawable.flag_ru
    }
}