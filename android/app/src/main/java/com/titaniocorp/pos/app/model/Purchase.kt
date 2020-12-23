package com.titaniocorp.pos.app.model

import com.titaniocorp.pos.database.entity.PurchaseEntity
import java.util.*

data class Purchase(
    var id: Long = 0,
    var customerId: Long = 0,
    var cost: Double = 0.0,
    var profit: Double = 0.0,
    var adjustment: Double = 0.0,
    var refund: Double = 0.0,
    var tax: Double = 0.0,
    var subtotal: Double = 0.0,
    var total: Double = 0.0,
    var isCredit: Boolean = false,
    var active: Boolean = true,
    var createdDate: Date = Date(),
    val payments: MutableList<PaymentPurchase> = mutableListOf(),
    val prices: MutableList<PricePurchase> = mutableListOf()
){
    fun compute(){
        cost = 0.0
        profit = 0.0
        tax = 0.0

        prices.forEach {
            cost += it.cost * it.quantity
            profit += it.profit * it.quantity
            tax += it.tax * it.quantity
        }

        adjustment = if(prices.isNullOrEmpty()) 0.0  else adjustment

        subtotal = cost + profit
        total = cost + profit + tax + adjustment
    }

    fun getTotalReal() = cost + profit + tax
}

fun Purchase.asDatabaseModel(): PurchaseEntity {
    return PurchaseEntity(
        id,
        customerId,
        cost,
        profit,
        adjustment,
        refund,
        tax,
        subtotal,
        total,
        isCredit,
        active,
        createdDate
    )
}