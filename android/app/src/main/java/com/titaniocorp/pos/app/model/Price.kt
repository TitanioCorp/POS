package com.titaniocorp.pos.app.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "price",
    indices = [
        Index(value = ["price_id"], unique = true),
        Index(value = ["product_id"])
    ]
)
data class Price(
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