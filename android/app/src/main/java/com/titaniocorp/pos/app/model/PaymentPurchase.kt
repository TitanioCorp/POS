package com.titaniocorp.pos.app.model

import androidx.room.*
import java.util.*

@Entity(
    tableName = "payment_purchase",
    indices = [
        Index(value = ["payment_purchase_id"], unique = true),
        Index(value = ["purchase_id"]),
        Index(value = ["payment_category_id"])
    ],
    foreignKeys = [
        ForeignKey(entity = PurchaseEntity::class,
            parentColumns = ["purchase_id"],
            childColumns = ["purchase_id"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class PaymentPurchase(
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