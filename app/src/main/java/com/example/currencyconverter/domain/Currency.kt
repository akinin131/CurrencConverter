package com.example.currencyconverter.domain

import android.content.Context
import com.example.currencyconverter.R
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
            ID.contains("R01235") -> {Country.USA}
            Name.contains("Евро") -> Country.EURO
            Name.contains("Армянских драмов") -> Country.ARMENIA
            Name.contains("Грузинский лари") -> Country.GEORGIA
            Name.contains("Рубль") -> Country.RU
            Name.contains("Австралийский доллар") -> Country.AUSTRALIA
            Name.contains("Азербайджанский манат") -> Country.AZERBOIJANI
            Name.contains("Фунт стерлингов Соединенного королевства") -> Country.GREAT_BRITIAN
            Name.contains("Белорусский рубль") -> Country.BELARUS
            Name.contains("Болгарский лев") -> Country.BULGARIAN
            Name.contains("Бразильский реал") -> Country.BRAZIL
            Name.contains("Венгерских форинтов") -> Country.HUNGARIAN
            Name.contains("Вьетнамских донгов") -> Country.VIETNAM
            Name.contains("Гонконгский доллар") -> Country.HONGKONG
            Name.contains("Датская крона") -> Country.DAT
            Name.contains("Дирхам ОАЭ") -> Country.ARAB
            Name.contains("Египетских фунтов") -> Country.EGYPT
            Name.contains("Индийских рупий") -> Country.INDIA
            Name.contains("Индонезийских рупий") -> Country.INDONESIA
            Name.contains("Казахстанских тенге") -> Country.KAZAKHSTAN
            Name.contains("Канадский доллар") -> Country.CANADA
            Name.contains("Катарский риал") -> Country.QATAR
            Name.contains("Киргизских сомов") -> Country.KYRGYZSTAN
            Name.contains("Китайский юань") -> Country.CHINA
            Name.contains("Молдавских леев") -> Country.MOLDOVA
            Name.contains("Новозеландский доллар") -> Country.NEW_ZEALAND
            Name.contains("Норвежских крон") -> Country.NORWAY
            Name.contains("Польский злотый") -> Country.POLAND
            Name.contains("Румынский лей") -> Country.ROMANIA
            Name.contains("Сингапурский доллар") -> Country.SINGAPURE
            Name.contains("Таджикских сомони") -> Country.TAJIKISTAN
            Name.contains("Таиландских батов") -> Country.THAILAND
            Name.contains("Новый туркменский манат") -> Country.TURKMENISTAN
            Name.contains("Турецких лир") -> Country.TURKEY
            Name.contains("Узбекских сумов") -> Country.UZBEKISTAN
            Name.contains("Украинских гривен") -> Country.UKRAINE
            Name.contains("Чешских крон") -> Country.CZECH
            Name.contains("Шведских крон") -> Country.SWEDEN
            Name.contains("Швейцарский франк") -> Country.SWITZERLAND
            Name.contains("Сербских динаров") -> Country.SERBIA
            Name.contains("Южноафриканских рэндов") -> Country.UAR
            Name.contains("Вон Республики Корея") -> Country.SOUTH_KOREA
            Name.contains("Японских иен") -> Country.JAPAN
            else -> Country.USA
        }
    }

    fun getNameValute(): Int {
        return when {
            ID.contains("R01235") -> R.string.valute_name_usa
            Name.contains("Евро") -> R.string.valute_name_euro
            Name.contains("Армянских драмов") -> R.string.valute_name_armenia
            Name.contains("Грузинский лари") -> R.string.valute_name_georgia
            Name.contains("Рубль") -> R.string.valute_name_ruble
            Name.contains("Австралийский доллар") -> R.string.valute_name_australia
            Name.contains("Азербайджанский манат") -> R.string.valute_name_azerbaijani
            Name.contains("Фунт стерлингов Соединенного королевства") -> R.string.valute_name_great_britain
            Name.contains("Белорусский рубль") -> R.string.valute_name_belarus
            Name.contains("Болгарский лев") -> R.string.valute_name_bulgarian
            Name.contains("Бразильский реал") -> R.string.valute_name_brazil
            Name.contains("Венгерских форинтов") -> R.string.valute_name_hungarian
            Name.contains("Вьетнамских донгов") -> R.string.valute_name_vietnam
            Name.contains("Гонконгский доллар") -> R.string.valute_name_hongkong
            Name.contains("Датская крона") -> R.string.valute_name_dat
            Name.contains("Дирхам ОАЭ") -> R.string.valute_name_arab
            Name.contains("Египетских фунтов") -> R.string.valute_name_egypt
            Name.contains("Индийских рупий") -> R.string.valute_name_india
            Name.contains("Индонезийских рупий") -> R.string.valute_name_indonesia
            Name.contains("Казахстанских тенге") -> R.string.valute_name_kazakhstan
            Name.contains("Канадский доллар") -> R.string.valute_name_canada
            Name.contains("Катарский риал") -> R.string.valute_name_qatar
            Name.contains("Киргизских сомов") -> R.string.valute_name_kyrgyzstan
            Name.contains("Китайский юань") -> R.string.valute_name_china
            Name.contains("Молдавских леев") -> R.string.valute_name_moldova
            Name.contains("Новозеландский доллар") -> R.string.valute_name_new_zealand
            Name.contains("Норвежских крон") -> R.string.valute_name_norway
            Name.contains("Польский злотый") -> R.string.valute_name_poland
            Name.contains("Румынский лей") -> R.string.valute_name_romania
            Name.contains("Сингапурский доллар") -> R.string.valute_name_singapure
            Name.contains("Таджикских сомони") -> R.string.valute_name_tajikistan
            Name.contains("Таиландских батов") -> R.string.valute_name_thailand
            Name.contains("Новый туркменский манат") -> R.string.valute_name_turkmenistan
            Name.contains("Турецких лир") -> R.string.valute_name_turkey
            Name.contains("Узбекских сумов") -> R.string.valute_name_uzbekistan
            Name.contains("Украинских гривен") -> R.string.valute_name_ukraine
            Name.contains("Чешских крон") -> R.string.valute_name_czech
            Name.contains("Шведских крон") -> R.string.valute_name_sweden
            Name.contains("Швейцарский франк") -> R.string.valute_name_switzerland
            Name.contains("Сербских динаров") -> R.string.valute_name_serbia
            Name.contains("Южноафриканских рэндов") -> R.string.valute_name_uar
            Name.contains("Вон Республики Корея") -> R.string.valute_name_south_korea
            Name.contains("Японских иен") -> R.string.valute_name_japan
            else -> R.string.valute_name_usa
        }
    }

}