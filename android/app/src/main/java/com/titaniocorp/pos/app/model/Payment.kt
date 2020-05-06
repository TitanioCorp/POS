package com.titaniocorp.pos.app.model

import java.util.*

data class Payment(
    var id: Long = 0,
    var paymentCategoryId: Long = 1,
    var value: Double = 0.0,
    var observation: String = "",
    var date: Date = Date()
)

fun Payment.asDatabaseModel(): PaymentEntity {
    return PaymentEntity(id, paymentCategoryId, value, observation, date)
}

fun List<Payment>.asDatabaseModel(): List<PaymentEntity> {
    return map{ PaymentEntity(it.id, it.paymentCategoryId, it.value, it.observation, it.date) }
}