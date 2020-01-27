package com.fangpu.ilovezappos.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fangpu.ilovezappos.network.BitstampApi
import com.fangpu.ilovezappos.network.OrdersBook
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class OrderBookViewModel : ViewModel() {

    private val _info = MutableLiveData<OrdersBook>()

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    val info: LiveData<OrdersBook>
        get() = _info

    init {
        getInternetData()
    }

    fun getInternetData() {
        coroutineScope.launch{
            val getOrderBookDeferred = BitstampApi.retrofitService.getOrderBook()
            try{
                val result = getOrderBookDeferred.await()
                _info.value = result
                Log.i("OrderBook", result.toString())
            } catch (e: Exception){
                _info.value = null
                Log.e("OrderBook", "No data retrieved")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}