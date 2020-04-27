package com.titaniocorp.pos.app.model

import androidx.room.*
import java.util.*

@Entity(
    tableName = "price_stock",
    indices = [
        Index(value = ["price_stock_id"], unique = true),
        Index(value = ["stock_id"]),
        Index(value = ["price_id"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = Stock::class,
            parentColumns = ["stock_id"],
            childColumns = ["stock_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Price::class,
            parentColumns = ["price_id"],
            childColumns = ["price_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PriceStock(
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
    @Ignore
    var isInitialProfit: Boolean = false,


    @ColumnInfo(name = "price_cost")
    @Ignore
    var priceCost: Double = 0.0,

    @Ignore
    var priceTax: Double = 0.0,

    @Ignore
    @ColumnInfo(name = "product_name")
    var productName: String = "",

    @Ignore
    @ColumnInfo(name = "price_name")
    var priceName: String = ""
)