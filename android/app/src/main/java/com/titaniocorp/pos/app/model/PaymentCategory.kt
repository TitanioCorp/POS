package com.titaniocorp.pos.app.model

import androidx.room.*
import java.util.*

@Entity(
    tableName = "payment_category",
    indices = [
        Index(value = ["payment_category_id"], unique = true)
    ]
)
data class PaymentCategory(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "payment_category_id")
    var id: Long = 0,

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "active")
    var isActive: Boolean = true,

    @ColumnInfo(name = "date")
    var date: Date = Date()
)