package com.titaniocorp.pos.database.entity

import androidx.room.*
import com.titaniocorp.pos.app.model.PaymentPurchase
import java.util.*

@Entity(
    tableName = "payment_purchase",
    indices = [
        Index(value = ["payment_purchase_id"], unique = true),
        Index(value = ["purchase_id"]),
        Index(value = ["payment_category_id"]),
        Index(value = ["created_date"], unique = true)
    ],
    foreignKeys = [
        ForeignKey(entity = PurchaseEntity::class,
            parentColumns = ["purchase_id"],
            childColumns = ["purchase_id"]
        )
    ])
data class PaymentPurchaseEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "payment_purchase_id")
    var id: Long = 0,

    @ColumnInfo(name = "purchase_id")
    var purchaseId: Long = 0,

    @ColumnInfo(name = "payment_category_id")
    var paymentCategoryId: Long = 1,

    @ColumnInfo(name = "value")
    var value: Double = 0.0,

    @ColumnInfo(name = "is_credit")
    var isCredit: Boolean = false,

    @ColumnInfo(name = "created_date")
    var createdDate: Date = Date()
)

fun List<PaymentPurchaseEntity>.asDomainModel(): List<PaymentPurchase>{
    return map {
        PaymentPurchase(
            it.id,
            it.purchaseId,
            it.paymentCategoryId,
            it.value,
            it.isCredit,
            it.createdDate
        )
    }
}

fun PaymentPurchaseEntity.asDomainModel(): PaymentPurchase {
    return PaymentPurchase(
        id,
        purchaseId,
        paymentCategoryId,
        value,
        isCredit,
        createdDate
    )
}