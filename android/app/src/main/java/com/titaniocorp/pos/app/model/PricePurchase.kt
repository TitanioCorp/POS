package com.titaniocorp.pos.app.model

import androidx.room.*
import java.util.*

@Entity(
    tableName = "price_purchase",
    indices = [
        Index(value = ["price_purchase_id"], unique = true),
        Index(value = ["price_id"]),
        Index(value = ["purchase_id"])
    ],
    foreignKeys = [
        ForeignKey(entity = Purchase::class,
            parentColumns = ["purchase_id"],
            childColumns = ["purchase_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(entity = Price::class,
            parentColumns = ["price_id"],
            childColumns = ["price_id"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class PricePurchase(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "price_purchase_id")
    var id: Long = 0,

    @ColumnInfo(name = "price_id")
    var priceId: Long = 0,

    @ColumnInfo(name = "purchase_id")
    var purchaseId: Long = 0,

    @ColumnInfo(name = "cost")
    var cost: Double = 0.0,

    @ColumnInfo(name = "profit")
    var profit: Double = 0.0,

    @ColumnInfo(name = "tax")
    var tax: Double = 0.0,

    @ColumnInfo(name = "quantity")
    var quantity: Int = 1,

    @ColumnInfo(name = "refund")
    var isRefund: Boolean = false,

    @ColumnInfo(name = "created_date")
    var createdDate: Date = Date()
)