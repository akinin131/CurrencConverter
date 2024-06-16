package com.example.currencyconverter.data.room

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.currencyconverter.utils.Country
@Entity(
    tableName = "selected_field",
    indices = [Index(value = ["currencyId"], unique = true)]
)
data class SelectedCurrency(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val currencyId: String,
    val name: String,
    val countryEnum: Country,
    val country: Int?,
    val value: Double?,
    val nominal: Int?
)