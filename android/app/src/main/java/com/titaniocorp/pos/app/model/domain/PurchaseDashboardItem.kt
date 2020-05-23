package com.titaniocorp.pos.app.model.domain

import androidx.room.ColumnInfo
import java.util.*

data class PurchaseDashboardItem(
    @ColumnInfo(name = "purchase_id")
    val purchaseId: Long,

    @ColumnInfo(name = "total")
    val total: Double,

    @ColumnInfo(name = "customer_id")
    val customerId: Long,

    @ColumnInfo(name = "created_date")
    val createdDate: Date,

    @ColumnInfo(name = "receivable")
    val receivable: Double,

    @ColumnInfo(name = "name")
    val name: String
)