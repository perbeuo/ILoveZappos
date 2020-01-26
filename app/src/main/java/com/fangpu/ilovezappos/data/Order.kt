package com.fangpu.ilovezappos.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
    var bid: String,
    var bid_amount: String,
    var ask: String,
    var ask_amount: String
) : Parcelable