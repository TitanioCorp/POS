package com.titaniocorp.pos.app.model.dto

import androidx.room.*
import com.titaniocorp.pos.app.model.PriceStock
import java.util.*

data class PriceStockDto(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "price_stock_id")
    var id: Long = 0,

    @ColumnInfo(name = "stock_id")
    var stockId: Long = 0,

    @ColumnInfo(name = "price_id")
    var priceId: Long = 0,

    @ColumnInfo(name = "quantity")
    var quantity: Int = 0,

    @ColumnInfo(name = "refund")
    var isRefund: Boolean = false,

    /* DTO */
    @ColumnInfo(name = "initial_profit")
    var isInitialProfit: Boolean = false,

    @ColumnInfo(name = "price_cost")
    var priceCost: Double = 0.0,

    @Ignore
    var priceTax: Double = 0.0,

    @ColumnInfo(name = "product_name")
    var productName: String = "",

    @ColumnInfo(name = "price_name")
    var priceName: String = ""
){
    fun toPriceStock(): PriceStock = PriceStock(id, stockId, priceId, quantity, isRefund, isInitialProfit, priceCost, priceTax, productName, priceName)
}