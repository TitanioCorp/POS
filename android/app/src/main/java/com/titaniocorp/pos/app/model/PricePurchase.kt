package com.titaniocorp.pos.app.model

import java.util.*

data class PricePurchase(
    var id: Long = 0,
    var priceId: Long = 0,
    var purchaseId: Long = 0,
    var cost: Double = 0.0,
    var profit: Double = 0.0,
    var tax: Double = 0.0,
    var quantity: Int = 1,
    var isRefund: Boolean = false,
    var createdDate: Date = Date()
)

fun PricePurchase.asDatabaseModel(): PricePurchaseEntity {
    return PricePurchaseEntity(id, priceId, purchaseId, cost, profit, tax, quantity, isRefund, createdDate)
}

fun List<PricePurchase>.asDomainModel(): List<PricePurchaseEntity>{
    return map { PricePurchaseEntity(it.id, it.priceId, it.purchaseId, it.cost, it.profit, it.tax, it.quantity, it.isRefund, it.createdDate) }
}