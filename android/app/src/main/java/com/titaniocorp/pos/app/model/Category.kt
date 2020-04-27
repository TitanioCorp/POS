package com.titaniocorp.pos.app.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "category",
    indices = [
        Index(value = ["category_id"], unique = true)
    ]
)
data class Category(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "category_id")
    var id: Long ?= null,

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "active")
    var active: Boolean = true
)