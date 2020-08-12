package com.titaniocorp.pos.database.entity

import androidx.room.*
import com.titaniocorp.pos.app.model.Profit

@Entity(
    tableName = "profit",
    indices = [
        Index(value = ["profit_id"], unique = true)
    ]
)
data class ProfitEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "profit_id")
    var id: Long = 0,

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "percent")
    var percent: Double = 0.0
)

fun List<ProfitEntity>.asDomainModel(): List<Profit>{
    return map {
        Profit(
            it.id,
            it.name,
            it.percent
        )
    }
}

fun ProfitEntity.asDomainModel(): Profit {
    return Profit(id, name, percent)
}