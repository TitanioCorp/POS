package com.titaniocorp.pos.app.model.domain

import com.titaniocorp.pos.database.entity.CustomerEntity

data class Customer(
    var id: Long = 0,
    var name: String = "",
    var dni: Long = 0,
    var phone: Long = 0,
    var email: String = "",
    var active: Boolean = true
)

fun Customer.asDatabaseModel(): CustomerEntity {
    return CustomerEntity(
        id = id,
        name = name,
        dni = dni,
        phone = phone,
        email = email,
        active = active
    )
}