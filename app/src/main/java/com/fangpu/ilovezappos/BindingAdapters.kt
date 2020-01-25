package com.fangpu.ilovezappos

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.fangpu.ilovezappos.network.BitstampPrice


@BindingAdapter("welcomeInfo")
fun bindInfo(infoText: TextView, info: List<BitstampPrice>?) {
    infoText.text = info?.get(0)?.price
}
