package com.titaniocorp.pos.app.model.dto

import androidx.room.ColumnInfo
import java.util.*

data class PurchaseDTO(
    @ColumnInfo(name = "purchase_id")
    val id: Long,

    @ColumnInfo(name = "customer_id")
    val customerId: Long,

    @ColumnInfo(name = "is_credit")
    val isCredit: Boolean = false,

    @ColumnInfo(name = "total")
    val total: Double = 0.0,

    @ColumnInfo(name = "created_date")
    val date: Date = Date()
)