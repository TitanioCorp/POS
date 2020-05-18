package com.titaniocorp.pos.app.model

import java.util.*

data class SearchPurchase(
    val type: Int,
    val customerId: Long,
    val startDate: Long,
    val finishDate: Long
)

enum class TypeSearchPurchase(val value: Int){
    ALL(0),
    RECEIVABLE(1),
}