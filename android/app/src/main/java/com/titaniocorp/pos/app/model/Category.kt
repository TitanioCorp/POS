package com.titaniocorp.pos.app.model

import com.titaniocorp.pos.database.entity.CategoryEntity

data class Category(
    var id: Long ?= null,
    var name: String = "",
    var active: Boolean = true
)

fun Category.asDatabaseModel(): CategoryEntity {
    return CategoryEntity(id, name, active)
}