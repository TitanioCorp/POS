package com.titaniocorp.pos.app.model

import java.util.*

data class Price(
    val id: Long,
    var productId: Long ?= null,
    var name: String,
    var sku: String,
    var cost: Double,
    var stock: Int,
    var isInitialProfit: Boolean,
    var active: Boolean,
    var createdDate: Date = Date()
)

fun Price.asDatabaseModel(): PriceEntity {
    return PriceEntity(id, productId, name, sku, cost, stock, isInitialProfit, active, createdDate)
}

fun List<Price>.asDatabaseModel(): List<PriceEntity> {
    return map { PriceEntity(
        it.id,
        it.productId,
        it.name,
        it.sku,
        it.cost,
        it.stock,
        it.isInitialProfit,
        it.active,
        it.createdDate)
    }
}