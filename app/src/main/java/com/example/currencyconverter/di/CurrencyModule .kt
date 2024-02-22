package com.example.currencyconverter.di

import com.example.currencyconverter.data.network.CurrencyApiService
import com.example.currencyconverter.data.repository.CurrencyRepositoryImpl
import com.example.currencyconverter.domain.CurrencyRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCurrencyRepository(apiService: CurrencyApiService): CurrencyRepository {
        return CurrencyRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideCurrencyApiService(): CurrencyApiService {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .baseUrl("https://www.cbr-xml-daily.ru/") // Замените на ваш базовый URL
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(CurrencyApiService::class.java)
    }
}




