package com.titaniocorp.pos.app.model

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import java.util.*

@Entity(tableName = "product",
    indices = [
        Index(value = ["product_id"], unique = true),
        Index(value = ["category_id"])
    ],
    foreignKeys = [
            ForeignKey(entity = CategoryEntity::class,
            parentColumns = ["category_id"],
            childColumns = ["category_id"],
            onDelete = CASCADE)
    ]
)
data class Product(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "product_id")
    var id: Long = 0,

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "category_id")
    var categoryId: Long = 0,

    @ColumnInfo(name = "description")
    var description: String = "",

    @ColumnInfo(name = "active")
    var active: Boolean = true,

    @ColumnInfo(name = "created_date")
    var createdDate: Date = Date(),

    @Ignore
    @ColumnInfo(name = "prices")
    val prices: MutableList<Price> = mutableListOf()
)