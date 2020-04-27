package com.titaniocorp.pos.app.model

import androidx.room.*
import java.util.*

@Entity(
    tableName = "stock",
    indices = [
        Index(value = ["stock_id"], unique = true)
    ]
)
data class Stock(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "stock_id")
    var id: Long = 0,

    @ColumnInfo(name = "purchase_ref")
    var purchaseRef: String = "",

    @ColumnInfo(name = "cost")
    var cost: Double = 0.0,

    @ColumnInfo(name = "tax")
    var tax: Double = 0.0,

    @ColumnInfo(name = "total")
    var total: Double = 0.0,

    @ColumnInfo(name = "date")
    var date: Date = Date(),

    @Ignore
    var prices: MutableList<PriceStock> = mutableListOf()
)