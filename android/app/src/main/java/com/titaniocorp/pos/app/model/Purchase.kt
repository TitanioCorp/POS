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
)

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