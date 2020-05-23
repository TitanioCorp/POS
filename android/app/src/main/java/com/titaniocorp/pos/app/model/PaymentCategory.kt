package com.titaniocorp.pos.app.model

import java.util.*

data class PaymentCategory(
    var id: Long = 0,
    var name: String = "",
    var isActive: Boolean = true,
    var date: Date = Date()
)

fun PaymentCategory.asDatabaseModel(): PaymentCategoryEntity {
    return PaymentCategoryEntity(id, name, isActive, date)
}

fun List<PaymentCategory>.asDatabaseModel(): List<PaymentCategoryEntity> {
    return map{ PaymentCategoryEntity(it.id, it.name, it.isActive, it.date) }
}