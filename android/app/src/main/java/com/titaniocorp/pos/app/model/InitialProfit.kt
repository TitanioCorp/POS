package com.titaniocorp.pos.app.model

import com.titaniocorp.pos.database.entity.InitialProfitEntity

data class InitialProfit(
    var id: Long = 0,
    var name: String = "",
    var percent: Double = 0.0
){
    companion object{
        const val DEFAULT_INITIAL_PROFIT_ID: Long = 1
    }
}

fun InitialProfit.asDatabaseModel(): InitialProfitEntity {
    return InitialProfitEntity(id, name, percent)
}
