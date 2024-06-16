package com.example.currencyconverter.data

import android.content.Context
import androidx.room.Room
import com.example.currencyconverter.data.room.AppDatabase
import com.example.currencyconverter.data.room.CurrencyFieldDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase ::class.java,
            "currency_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideCurrencyFieldDao(database: AppDatabase): CurrencyFieldDao {
        return database.currencyFieldDao()
    }
}
