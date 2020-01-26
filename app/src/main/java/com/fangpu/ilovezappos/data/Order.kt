package com.fangpu.ilovezappos.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
    var price: String,
    var amount: String
) : Parcelable