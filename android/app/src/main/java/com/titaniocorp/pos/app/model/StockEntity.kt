package com.titaniocorp.pos.app.model

import androidx.room.*
import java.util.*

@Entity(
    tableName = "stock",
    indices = [
        Index(value = ["stock_id"], unique = true)
    ]
)
data class StockEntity(
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
    var date: Date = Date()
)

fun List<StockEntity>.asDomainModel(): List<Stock>{
    return map { Stock(it.id, it.purchaseRef, it.cost, it.tax, it.total, it.date) }
}

fun StockEntity.asDomainModel(): Stock{
    return Stock(id, purchaseRef, cost, tax, total, date)
}