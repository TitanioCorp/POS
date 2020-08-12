package com.titaniocorp.pos.database.entity

import androidx.room.*
import java.util.*

@Entity(
    tableName = "billing",
    indices = [
        Index(value = ["billing_id"], unique = true)
    ]
)
data class BillingEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "billing_id")
    var id: Long = 0,

    @ColumnInfo(name = "cost")
    var cost: Double = 0.0,

    @ColumnInfo(name = "profit")
    var profit: Double = 0.0,

    @ColumnInfo(name = "refund")
    var refund: Double = 0.0,

    @ColumnInfo(name = "payments")
    var payments: Double = 0.0,

    @ColumnInfo(name = "adjustment")
    var adjustment: Double = 0.0,

    @ColumnInfo(name = "tax")
    var tax: Double = 0.0,

    @ColumnInfo(name = "stock")
    var stock: Double = 0.0,

    @ColumnInfo(name = "expenses")
    var expenses: Double = 0.0,

    @ColumnInfo(name = "income")
    var income: Double = 0.0,

    @ColumnInfo(name = "total_purchase")
    var totalPurchase: Double = 0.0,

    @ColumnInfo(name = "total_expenses")
    var totalExpenses: Double = 0.0,

    @ColumnInfo(name = "total_income")
    var totalIncome: Double = 0.0,

    @ColumnInfo(name = "total")
    var total: Double = 0.0,

    @ColumnInfo(name = "created_date")
    var createdDate: Date = Date()
)