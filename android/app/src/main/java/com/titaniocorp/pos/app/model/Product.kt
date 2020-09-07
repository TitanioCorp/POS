package com.titaniocorp.pos.app.model

import com.titaniocorp.pos.database.entity.ProductEntity
import java.util.*

data class Product(
    var id: Long = 0,
    var name: String = "",
    var categoryId: Long = 0,
    var description: String = "",
    var active: Boolean = true,
    var createdDate: Date = Date(),
    val prices: MutableList<Price> = mutableListOf()
)

fun Product.asDatabaseModel(): ProductEntity {
    return ProductEntity(
        id,
        name,
        categoryId,
        description,
        active,
        createdDate
    )
}
