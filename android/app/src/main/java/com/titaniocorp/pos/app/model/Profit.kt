package com.titaniocorp.pos.app.model

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(
    tableName = "profit",
    indices = [
        Index(value = ["profit_id"], unique = true)
    ]
)
data class Profit(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "profit_id")
    var id: Long = 0,

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "percent")
    var percent: Double = 0.0
)