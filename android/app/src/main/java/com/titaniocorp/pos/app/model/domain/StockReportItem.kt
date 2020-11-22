package com.titaniocorp.pos.app.model.domain

import androidx.room.ColumnInfo
import androidx.room.Ignore
import com.titaniocorp.pos.util.calculateTax

data class StockReportItem(
    @ColumnInfo(name = "price_id")
    val priceId: Long,

    @ColumnInfo(name = "product_name")
    val productName: String,

    @ColumnInfo(name = "price_name")
    val priceName: String,

    @ColumnInfo(name = "cost")
    val cost: Double,

    @ColumnInfo(name = "stock")
    val stock: Int,

    @ColumnInfo(name = "initial_profit")
    val isInitialProfit: Boolean
){
    @Ignore
    val tax = cost.calculateTax()

    @Ignore
    val total = (cost + tax) * stock

    @Ignore
    var isSelected = false
}