package com.example.currencyconverter.botton_navigation

import com.example.currencyconverter.R

sealed class BottomItem(val tittle: String, val itemId : Int, val route : String){
    object screen1: BottomItem("Конвертер", R.drawable.baseline_attach_money_24, "screen_1")
    object screen2: BottomItem("График", R.drawable.baseline_timeline_24, "screen_2")
}