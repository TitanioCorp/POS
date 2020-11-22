package com.titaniocorp.pos.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.titaniocorp.pos.app.model.Price
import java.util.*

@Entity(
    tableName = "price",
    indices = [
        Index(value = ["price_id"], unique = true),
        Index(value = ["product_id"]),
        Index(value = ["created_date"], unique = true)
    ]
)
data class PriceEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "price_id")
    val id: Long,

    @ColumnInfo(name = "product_id")
    var productId: Long ?= null,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "sku")
    var sku: String,

    @ColumnInfo(name = "cost")
    var cost: Double,

    @ColumnInfo(name = "stock")
    var stock: Int,

    @ColumnInfo(name = "initial_profit")
    var isInitialProfit: Boolean,

    @ColumnInfo(name = "active")
    var active: Boolean,

    @ColumnInfo(name = "created_date")
    var createdDate: Date = Date()
)

fun List<PriceEntity>.asDomainModel(): List<Price>{
    return map {
        Price(
            it.id,
            it.productId,
            0,
            it.isInitialProfit,
            it.name,
            it.sku,
            it.cost,
            it.stock,
            it.active,
            it.createdDate
        )
    }
}

fun PriceEntity.asDomainModel(): Price {
    return Price(
        id,
        productId,
        0,
        isInitialProfit,
        name,
        sku,
        cost,
        stock,
        active,
        createdDate
    )
}