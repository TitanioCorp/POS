package com.titaniocorp.pos.app.model

import com.titaniocorp.pos.database.entity.ProfitEntity

data class Profit(
    var id: Long = 0,
    var name: String = "",
    var percent: Double = 0.0
)

fun Profit.asDatabaseModel(): ProfitEntity {
    return ProfitEntity(id, name, percent)
}