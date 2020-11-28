package com.titaniocorp.pos.app.model

import com.titaniocorp.pos.database.entity.PriceEntity
import com.titaniocorp.pos.util.calculateTax
import com.titaniocorp.pos.util.calculateTotalReal
import java.util.*

data class Price(
    val id: Long = 0,
    var productId: Long? = 0,
    var initialProfitId: Long? = 0,
    var isInitialProfit: Boolean = true,
    var name: String = "",
    var sku: String = "",
    var cost: Double = 0.0,
    var stock: Int = 0,
    var active: Boolean = true,
    var createdDate: Date = Date()
){
    var initialProfitSelected: InitialProfit ?= null
    var totalBill: Double = 0.0
    var tax: Double = 0.0
    var totalUnit: Double = 0.0
    var total: Double = 0.0

    fun compute(){
        totalBill = cost.calculateTotalReal(initialProfitSelected?.percent)
        tax = cost.calculateTax(initialProfitSelected?.percent)
        totalUnit = cost + tax
        total = (cost + tax) * stock
    }
}

fun Price.asDatabaseModel(): PriceEntity {
    return PriceEntity(
        id,
        productId,
        name,
        sku,
        cost,
        stock,
        isInitialProfit,
        active,
        createdDate,
        initialProfitId
    )
}

fun List<Price>.asDatabaseModel(): List<PriceEntity> {
    return map {
        PriceEntity(
            it.id,
            it.productId,
            it.name,
            it.sku,
            it.cost,
            it.stock,
            it.isInitialProfit,
            it.active,
            it.createdDate,
            it.initialProfitId
        )
    }
}