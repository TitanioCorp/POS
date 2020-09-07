package com.titaniocorp.pos.app.model

import com.titaniocorp.pos.database.entity.StockEntity
import java.util.*

data class Stock(
    var id: Long = 0,
    var purchaseRef: String = "",
    var cost: Double = 0.0,
    var tax: Double = 0.0,
    var total: Double = 0.0,
    var date: Date = Date(),
    var prices: MutableList<PriceStock> = mutableListOf()
)

fun Stock.asDatabaseModel(): StockEntity {
    return StockEntity(
        id,
        purchaseRef,
        cost,
        tax,
        total,
        date
    )
}