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
            entity = StockEntity::class,
            parentColumns = ["stock_id"],
            childColumns = ["stock_id"]
        ),
        ForeignKey(
            entity = PriceEntity::class,
            parentColumns = ["price_id"],
            childColumns = ["price_id"]
        )
    ]
)
data class PriceStockEntity(
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
    var isRefund: Boolean = false
)

fun List<PriceStockEntity>.asDomainModel(): List<PriceStock>{
    return map { PriceStock(it.id, it.stockId, it.priceId, it.quantity, it.isRefund) }
}

fun PriceStockEntity.asDomainModel(): PriceStock{
    return PriceStock(id, stockId, priceId, quantity, isRefund)
}