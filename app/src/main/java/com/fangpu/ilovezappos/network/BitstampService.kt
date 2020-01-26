package com.fangpu.ilovezappos.network

import com.fangpu.ilovezappos.data.Order
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://www.bitstamp.net/api/v2/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface BitstampService{
    @GET("transactions/btcusd/")
    fun getPriceHistory():
            Deferred<List<BitstampPrice>>

    @GET("order_book/btcusd/")
    fun getOrderBook():
            Deferred<OrdersBook>
}

object BitstampApi {
    val retrofitService : BitstampService by lazy {
        retrofit.create(BitstampService::class.java)
    }
}