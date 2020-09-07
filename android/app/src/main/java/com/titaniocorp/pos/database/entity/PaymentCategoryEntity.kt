package com.titaniocorp.pos.database.entity

import androidx.room.*
import com.titaniocorp.pos.app.model.PaymentCategory
import java.util.*

@Entity(
    tableName = "payment_category",
    indices = [
        Index(value = ["payment_category_id"], unique = true)
    ]
)
data class PaymentCategoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "payment_category_id")
    var id: Long = 0,

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "active")
    var isActive: Boolean = true,

    @ColumnInfo(name = "date")
    var date: Date = Date()
)

fun List<PaymentCategoryEntity>.asDomainModel(): List<PaymentCategory>{
    return map {
        PaymentCategory(
            it.id,
            it.name,
            it.isActive,
            it.date
        )
    }
}

fun PaymentCategoryEntity.asDomainModel(): PaymentCategory {
    return PaymentCategory(
        id,
        name,
        isActive,
        date
    )
}