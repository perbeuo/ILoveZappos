package com.fangpu.ilovezappos.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://www.bitstamp.net/api/v2/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface BitstampService{
    @GET("transactions/btcusd/")
    fun getPriceHistory():
        Call<String>
}

object BitstampApi {
    val retrofitService : BitstampService by lazy {
        retrofit.create(BitstampService::class.java)
    }
}