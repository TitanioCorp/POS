package com.titaniocorp.pos.app.model

data class PriceStock(
    var id: Long = 0,
    var stockId: Long = 0,
    var priceId: Long = 0,
    var quantity: Int = 0,
    var isRefund: Boolean = false,
    var isInitialProfit: Boolean = false,
    var priceCost: Double = 0.0,
    var priceTax: Double = 0.0,
    var productName: String = "",
    var priceName: String = ""
)

fun PriceStock.asDatabaseModel(): PriceStockEntity {
    return PriceStockEntity(id, stockId, priceId, quantity, isRefund)
}

fun List<PriceStock>.asDomainModel(): List<PriceStockEntity>{
    return map { PriceStockEntity(it.id, it.stockId, it.priceId, it.quantity, it.isRefund) }
}