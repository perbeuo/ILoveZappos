package com.fangpu.ilovezappos.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fangpu.ilovezappos.network.BitstampApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _info = MutableLiveData<String>()

    val info: LiveData<String>
        get() = _info

    init {
        getInternetData()
    }

    private fun getInternetData() {
        BitstampApi.retrofitService.getPriceHistory().enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                _info.value = t.message
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                _info.value = response.body()
            }
        })
        _info.value = "Hello Fragment"
    }
}
