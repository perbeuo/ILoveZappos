package com.fangpu.ilovezappos.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fangpu.ilovezappos.network.BitstampApi
import com.fangpu.ilovezappos.network.BitstampPrice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class PriceHistoryViewModel : ViewModel() {

    private val _info = MutableLiveData<List<BitstampPrice>>()

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    val info: LiveData<List<BitstampPrice>>
        get() = _info

    init {
        getInternetData()
    }

    private fun getInternetData() {
        coroutineScope.launch{
            var getPriceHistoryDeferred = BitstampApi.retrofitService.getPriceHistory()
            try{
                val listResult = getPriceHistoryDeferred.await()
                _info.value = listResult
            } catch (e: Exception){
                _info.value = ArrayList()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
