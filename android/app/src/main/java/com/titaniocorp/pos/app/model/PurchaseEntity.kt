package com.titaniocorp.pos.app.model

import androidx.room.*
import java.util.*

@Entity(
    tableName = "purchase",
    indices = [
        Index(value = ["purchase_id"], unique = true),
        Index(value = ["customer_id"])
    ]
)
data class PurchaseEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "purchase_id")
    var id: Long = 0,

    @ColumnInfo(name = "customer_id")
    var customerId: Long = 0,

    @ColumnInfo(name = "cost")
    var cost: Double = 0.0,

    @ColumnInfo(name = "profit")
    var profit: Double = 0.0,

    @ColumnInfo(name = "adjustment")
    var adjustment: Double = 0.0,

    @ColumnInfo(name = "refund")
    var refund: Double = 0.0,

    @ColumnInfo(name = "tax")
    var tax: Double = 0.0,

    @ColumnInfo(name = "subtotal")
    var subtotal: Double = 0.0,

    @ColumnInfo(name = "total")
    var total: Double = 0.0,

    @ColumnInfo(name = "is_credit")
    var isCredit: Boolean = false,

    @ColumnInfo(name = "active")
    var active: Boolean = true,

    @ColumnInfo(name = "created_date")
    var createdDate: Date = Date()
)

fun List<PurchaseEntity>.asDomainModel(): List<Purchase>{
    return map { Purchase(
        it.id,
        it.customerId,
        it.cost,
        it.profit,
        it.adjustment,
        it.refund,
        it.tax,
        it.subtotal,
        it.total,
        it.isCredit,
        it.active,
        it.createdDate)
    }
}

fun PurchaseEntity.asDomainModel(): Purchase{
    return Purchase(
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