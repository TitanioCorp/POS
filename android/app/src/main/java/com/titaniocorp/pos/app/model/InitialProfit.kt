package com.titaniocorp.pos.app.model

import com.titaniocorp.pos.database.entity.InitialProfitEntity

data class InitialProfit(
    var id: Long = 0,
    var name: String = "",
    var percent: Double = 0.0
)

fun InitialProfit.asDatabaseModel(): InitialProfitEntity {
    return InitialProfitEntity(id, name, percent)
}
