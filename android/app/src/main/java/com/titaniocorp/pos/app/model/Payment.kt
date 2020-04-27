package com.titaniocorp.pos.app.model

import androidx.room.*
import java.util.*

@Entity(
    tableName = "payment",
    indices = [
        Index(value = ["payment_id"], unique = true),
        Index(value = ["payment_category_id"])
    ]
)
data class Payment(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "payment_id")
    var id: Long = 0,

    @ColumnInfo(name = "payment_category_id")
    var paymentCategoryId: Long = 1,

    @ColumnInfo(name = "value")
    var value: Double = 0.0,

    @ColumnInfo(name = "observation")
    var observation: String = "",

    @ColumnInfo(name = "date")
    var date: Date = Date()
)