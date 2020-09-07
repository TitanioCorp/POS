package com.titaniocorp.pos.database.entity

import androidx.room.*
import com.titaniocorp.pos.app.model.PricePurchase
import java.util.*

@Entity(
    tableName = "price_purchase",
    indices = [
        Index(value = ["price_purchase_id"], unique = true),
        Index(value = ["price_id"]),
        Index(value = ["purchase_id"]),
        Index(value = ["created_date"], unique = true)
    ],
    foreignKeys = [
        ForeignKey(entity = PurchaseEntity::class,
            parentColumns = ["purchase_id"],
            childColumns = ["purchase_id"]
        ),
        ForeignKey(entity = PriceEntity::class,
            parentColumns = ["price_id"],
            childColumns = ["price_id"]
        )
    ])
data class PricePurchaseEntity(
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

fun List<PricePurchaseEntity>.asDomainModel(): List<PricePurchase>{
    return map {
        PricePurchase(
            it.id,
            it.priceId,
            it.purchaseId,
            it.cost,
            it.profit,
            it.tax,
            it.quantity,
            it.isRefund,
            it.createdDate
        )
    }
}

fun PricePurchaseEntity.asDomainModel(): PricePurchase {
    return PricePurchase(
        id,
        priceId,
        purchaseId,
        cost,
        profit,
        tax,
        quantity,
        isRefund,
        createdDate
    )
}