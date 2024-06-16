package com.example.currencyconverter.data.room

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class CurrencyRepositoryRoom @Inject constructor(private val currencyFieldDao: CurrencyFieldDao) {

    suspend fun saveSelectedCurrency(currencyField: SelectedCurrency) {
        currencyFieldDao.insertSelectedCurrencyField(currencyField)
    }

    suspend fun saveSelectedCurrency(currencyField: List<SelectedCurrency>) {
        currencyFieldDao.insertSelectedCurrencyField(currencyField)
    }

    suspend fun saveCurrencyFields(currencyFields: List<CurrencyFieldEntity>) {
        currencyFieldDao.insertCurrencyFields(currencyFields)
    }

    suspend fun updateCurrencyField(currencyFields: List<CurrencyFieldEntity>) {
        currencyFields.forEach { currencyField ->
            currencyFieldDao.updateCurrencyFieldValue(currencyField.currencyId, currencyField.value!!)
        }
    }

    suspend fun updateSelectedCurrencyFieldList(currencyFields: List<SelectedCurrency>) {
        currencyFields.forEach { currencyField ->
            currencyFieldDao.updateSelectedCurrencyField(currencyField.currencyId, currencyField.value)
        }
    }

    suspend fun updateSelectedCurrencyField(currency: SelectedCurrency) {
        currencyFieldDao.updateSelectedCurrencyField(currency)
    }


    fun getSelectedCurrencyById(id: String): Flow<SelectedCurrency?> {
        return currencyFieldDao.getSelectedCurrencyFieldById(id)
    }

    fun getAllCurrencyFields(): Flow<List<CurrencyFieldEntity>> {
        return currencyFieldDao.getAllCurrencyFields()
    }

    fun getAllSelectedCurrencies(): Flow<List<SelectedCurrency?>> {
        return currencyFieldDao.getAllSelectedCurrencies()
    }

    suspend fun deleteSelectedCurrency(currency: SelectedCurrency) {
        currencyFieldDao.deleteSelectedCurrency(currency)
    }

    suspend fun updateCurrency(currency: CurrencyFieldEntity) {
        currencyFieldDao.updateCurrencyField(currency)
    }
}