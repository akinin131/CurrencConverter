package com.example.currencyconverter.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CurrencyFieldEntity::class, SelectedCurrency::class], version = 6)
abstract class AppDatabase : RoomDatabase() {
    abstract fun currencyFieldDao(): CurrencyFieldDao
}
