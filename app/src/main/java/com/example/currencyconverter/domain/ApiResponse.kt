package com.example.currencyconverter.domain

data class ApiResponse(
    val Date: String,
    val PreviousDate: String,
    val PreviousURL: String,
    val Timestamp: String,
    val Valute: Map<String, Currency> // Map для представления Valute объектов
)