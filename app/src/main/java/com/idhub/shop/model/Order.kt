package com.idhub.shop.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
    var id: Long,
    var product: Product,
    var recipientName: String,
    var recipientMobile: String,
    var recipientAddress: String,
    var status: Status
) : Parcelable {
    enum class Status {
        PACKAGE_PROCESSING,
        PACKAGE_TRANSPORTING,
        PACKAGE_ARRIVED,
        PACKAGE_DONE
    }
}