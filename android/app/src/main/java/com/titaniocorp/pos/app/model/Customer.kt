package com.titaniocorp.pos.app.model

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(
    tableName = "customer",
    indices = [
        Index(value = ["customer_id"], unique = true)
    ]
)
data class Customer(
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