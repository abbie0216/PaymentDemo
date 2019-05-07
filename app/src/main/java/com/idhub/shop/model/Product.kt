package com.idhub.shop.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    var id: Long,
    var name: String,
    var price: Int
) : Parcelable