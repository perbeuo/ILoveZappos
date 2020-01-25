package com.fangpu.ilovezappos.network

data class BitstampPrice(
    val date: String,
    val tid: String,
    val price: String,
    val type: String,
    val amount: String
)
