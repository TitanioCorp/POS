package com.titaniocorp.pos.app.model

import java.util.*

data class Billing(
    var id: Long = 0,
    var cost: Double = 0.0,
    var profit: Double = 0.0,
    var refund: Double = 0.0,
    var payments: Double = 0.0,
    var adjustment: Double = 0.0,
    var tax: Double = 0.0,
    var stock: Double = 0.0,
    var expenses: Double = 0.0,
    var income: Double = 0.0,
    var totalPurchase: Double = 0.0,
    var totalExpenses: Double = 0.0,
    var totalIncome: Double = 0.0,
    var total: Double = 0.0,
    var createdDate: Date = Date()
)