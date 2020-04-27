package com.titaniocorp.pos.app.model.dto

import androidx.room.ColumnInfo

data class SearchProductDTO(
    @ColumnInfo(name = "product_id")
    val productId: Long,

    @ColumnInfo(name = "price_id")
    val priceId: Long,

    @ColumnInfo(name = "product_name")
    val productName: String,

    @ColumnInfo(name = "price_name")
    val priceName: String
)