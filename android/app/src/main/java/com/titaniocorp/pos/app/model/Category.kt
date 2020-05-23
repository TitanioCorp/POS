package com.titaniocorp.pos.app.model

data class Category(
    var id: Long ?= null,
    var name: String = "",
    var active: Boolean = true
)

fun Category.asDatabaseModel(): CategoryEntity {
    return CategoryEntity(id, name, active)
}