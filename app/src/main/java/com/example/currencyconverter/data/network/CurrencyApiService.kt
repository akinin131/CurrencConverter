package com.example.currencyconverter.data.network

import com.example.currencyconverter.domain.ApiResponse
import retrofit2.Response
import retrofit2.http.GET

interface CurrencyApiService {
    @GET("daily_json.js")
    suspend fun getCurrencies(): Response<ApiResponse>
}