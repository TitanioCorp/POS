package com.titaniocorp.pos.database.entity

import androidx.room.*
import com.titaniocorp.pos.app.model.InitialProfit
import com.titaniocorp.pos.app.model.Profit

@Entity(
    tableName = "initial_profit",
    indices = [
        Index(value = ["initial_profit_id"], unique = true)
    ]
)
data class InitialProfitEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "initial_profit_id")
    var id: Long = 0,

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "percent")
    var percent: Double = 0.0
)

fun List<InitialProfitEntity>.asDomainModel(): List<InitialProfit>{
    return map {
        InitialProfit(
            it.id,
            it.name,
            it.percent
        )
    }
}

fun InitialProfitEntity.asDomainModel(): InitialProfit {
    return InitialProfit(id, name, percent)
}