package com.titaniocorp.pos.app.model

import java.util.*

data class PaymentPurchase(
    var id: Long = 0,
    var purchaseId: Long = 0,
    var paymentCategoryId: Long = 1,
    var value: Double = 0.0,
    var isCredit: Boolean = false,
    var createdDate: Date = Date()
)

fun PaymentPurchase.asDatabaseModel(): PaymentPurchaseEntity {
    return PaymentPurchaseEntity(id, purchaseId, paymentCategoryId, value, isCredit, createdDate)
}

fun List<PaymentPurchase>.asDatabaseModel(): List<PaymentPurchaseEntity> {
    return map{ PaymentPurchaseEntity(it.id, it.purchaseId, it.paymentCategoryId, it.value, it.isCredit, it.createdDate)}
}