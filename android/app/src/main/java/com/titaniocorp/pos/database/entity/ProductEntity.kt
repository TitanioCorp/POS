package com.titaniocorp.pos.database.entity

import androidx.room.*
import com.titaniocorp.pos.app.model.Product
import java.util.*

@Entity(tableName = "product",
    indices = [
        Index(value = ["product_id"], unique = true),
        Index(value = ["category_id"]),
        Index(value = ["created_date"], unique = true)
    ],
    foreignKeys = [
            ForeignKey(entity = CategoryEntity::class,
            parentColumns = ["category_id"],
            childColumns = ["category_id"])
    ]
)
data class ProductEntity(
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
    var createdDate: Date = Date()
)

fun List<ProductEntity>.asDomainModel(): List<Product>{
    return map {
        Product(
            it.id,
            it.name,
            it.categoryId,
            it.description,
            it.active,
            it.createdDate
        )
    }
}

fun ProductEntity.asDomainModel(): Product {
    return Product(
        id,
        name,
        categoryId,
        description,
        active,
        createdDate
    )
}