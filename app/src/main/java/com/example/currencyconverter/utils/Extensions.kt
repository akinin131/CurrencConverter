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
        Country.AUSTRALIA -> R.drawable.australia
        Country.AZERBOIJANI -> R.drawable.azerbaijani
        Country.GREAT_BRITIAN -> R.drawable.great_britian
        Country.BELARUS -> R.drawable.belarus
        Country.BULGARIAN -> R.drawable.bulgarian
        Country.BRAZIL -> R.drawable.brazil
        Country.HUNGARIAN -> R.drawable.hungarian
        Country.VIETNAM -> R.drawable.vietnam
        Country.HONGKONG -> R.drawable.hong_kong
        Country.DAT -> R.drawable.dat
        Country.ARAB -> R.drawable.arab
        Country.EGYPT -> R.drawable.egypt
        Country.INDIA -> R.drawable.india
        Country.INDONESIA -> R.drawable.indonesia
        Country.KAZAKHSTAN -> R.drawable.kazakhstan
        Country.CANADA -> R.drawable.canada
        Country.QATAR -> R.drawable.qatar
        Country.KYRGYZSTAN -> R.drawable.kyrgyzstan
        Country.CHINA -> R.drawable.china
        Country.MOLDOVA -> R.drawable.moldova
        Country.NEW_ZEALAND -> R.drawable.new_zealand
        Country.NORWAY -> R.drawable.norway
        Country.POLAND -> R.drawable.poland
        Country.ROMANIA -> R.drawable.romania
        Country.SINGAPURE -> R.drawable.singapore
        Country.TAJIKISTAN -> R.drawable.tajikistan
        Country.THAILAND -> R.drawable.thailand
        Country.TURKMENISTAN -> R.drawable.turkmenistan
        Country.TURKEY -> R.drawable.turkey
        Country.UZBEKISTAN -> R.drawable.uzbekistan
        Country.UKRAINE -> R.drawable.ukraine
        Country.CZECH -> R.drawable.czech
        Country.SWEDEN -> R.drawable.sweden
        Country.SWITZERLAND -> R.drawable.switzerland
        Country.SERBIA -> R.drawable.serbia
        Country.UAR -> R.drawable.uar
        Country.SOUTH_KOREA -> R.drawable.south_korea
        Country.JAPAN -> R.drawable.japan
    }
}