package com.titaniocorp.pos.database.entity

import androidx.room.*
import com.titaniocorp.pos.app.model.Payment
import java.util.*

@Entity(
    tableName = "payment",
    indices = [
        Index(value = ["payment_id"], unique = true),
        Index(value = ["payment_category_id"])
    ]
)
data class PaymentEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "payment_id")
    var id: Long = 0,

    @ColumnInfo(name = "payment_category_id")
    var paymentCategoryId: Long = 1,

    @ColumnInfo(name = "value")
    var value: Double = 0.0,

    @ColumnInfo(name = "observation")
    var observation: String = "",

    @ColumnInfo(name = "date")
    var date: Date = Date()
)

fun List<PaymentEntity>.asDomainModel(): List<Payment>{
    return map {
        Payment(
            it.id,
            it.paymentCategoryId,
            it.value,
            it.observation,
            it.date
        )
    }
}

fun PaymentEntity.asDomainModel(): Payment {
    return Payment(
        id,
        paymentCategoryId,
        value,
        observation,
        date
    )
}