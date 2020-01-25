package com.fangpu.ilovezappos

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter


@BindingAdapter("welcomeInfo")
fun bindInfo(infoText: TextView, info: String?) {
    if (info.equals("123123")){
        infoText.visibility = View.INVISIBLE
    }
    infoText.text = info + "--Adapter"
}
