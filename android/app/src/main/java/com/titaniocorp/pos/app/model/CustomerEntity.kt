package com.titaniocorp.pos.app.model

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.titaniocorp.pos.app.model.domain.Customer

@Entity(
    tableName = "customer",
    indices = [
        Index(value = ["customer_id"], unique = true)
    ]
)
data class CustomerEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "customer_id")
    var id: Long = 0,

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "dni")
    var dni: Long = 0,

    @ColumnInfo(name = "phone")
    var phone: Long = 0,

    @ColumnInfo(name = "email")
    var email: String = "",

    @ColumnInfo(name = "active")
    var active: Boolean = true
)

fun List<CustomerEntity>.asDomainModel(): List<Customer>{
    return map { Customer(
        id = it.id,
        name = it.name,
        dni = it.dni,
        phone = it.phone,
        email = it.email,
        active = it.active)
    }
}

fun CustomerEntity.asDomainModel(): Customer{
    return Customer(
        id = id,
        name = name,
        dni = dni,
        phone = phone,
        email = email,
        active =active
    )
}