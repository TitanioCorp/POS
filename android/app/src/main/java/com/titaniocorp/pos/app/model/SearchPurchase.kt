package com.titaniocorp.pos.app.model

import java.util.*

data class SearchPurchase(
    val type: TypeSearchPurchase,
    val customer: Long,
    val startDate: Date,
    val finishDate: Date
)

enum class TypeSearchPurchase{
    TODAY,
    ALL,
    RECEIVABLE,
}