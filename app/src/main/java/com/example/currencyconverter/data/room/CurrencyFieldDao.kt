package com.example.currencyconverter.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyFieldDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSelectedCurrencyField(currencyField: SelectedCurrency)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSelectedCurrencyField(currencyField: List<SelectedCurrency>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencyFields(currencyFields: List<CurrencyFieldEntity>)

    @Query("UPDATE currency_field SET value = :newValue WHERE currencyId = :currencyId")
    suspend fun updateCurrencyFieldValue(currencyId: String, newValue: Double)

    @Update
    suspend fun updateCurrencyField(currencyField: CurrencyFieldEntity)

    @Query("UPDATE selected_field SET value = :newValue WHERE currencyId = :currencyId")
    suspend fun updateSelectedCurrencyField(currencyId: String, newValue: Double?)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateSelectedCurrencyField(currencyField: SelectedCurrency)

    @Query("SELECT * FROM selected_field WHERE id = :id")
    fun getSelectedCurrencyFieldById(id: String): Flow<SelectedCurrency?>

    @Query("SELECT * FROM currency_field")
    fun getAllCurrencyFields(): Flow<List<CurrencyFieldEntity>>

    @Query("SELECT * FROM selected_field")
    fun getAllSelectedCurrencies(): Flow<List<SelectedCurrency>>

    @Delete
    suspend fun deleteSelectedCurrency(currency: SelectedCurrency)
}