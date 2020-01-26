package com.fangpu.ilovezappos.network

import com.fangpu.ilovezappos.data.Order

data class OrdersBook(
    val timestamp: String,
    val bids: List<List<String>>,
    val asks: List<List<String>>
)