package com.titaniocorp.pos.app.model.dto

import androidx.room.ColumnInfo
import java.util.*

data class DetailPurchaseAdapterDto(
    @ColumnInfo(name = "price_purchase_id")
    val pricePaymentId: Long,

    @ColumnInfo(name = "price_id")
    val priceId: Long,

    @ColumnInfo(name = "product_name")
    val nameProduct: String,

    @ColumnInfo(name = "price_name")
    val namePrice: String,

    @ColumnInfo(name = "created_date")
    val createdDate: Date,

    @ColumnInfo(name = "refund")
    val refund: Boolean,

    @ColumnInfo(name = "cost")
    val cost: Double,

    @ColumnInfo(name = "tax")
    val tax: Double,

    @ColumnInfo(name = "profit")
    val profit: Double,

    @ColumnInfo(name = "quantity")
    var quantity: Int
)