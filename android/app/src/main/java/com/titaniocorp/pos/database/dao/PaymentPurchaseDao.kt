package com.titaniocorp.pos.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.titaniocorp.pos.database.entity.PaymentPurchaseEntity

/**
 * Maneja las consultas a la base de datos de pelicula
 * @version 1.0
 * @author Juan Ortiz
 * @date 27/12/2019
 */
@Dao
interface PaymentPurchaseDao {
    /* SELECT */
    @Query("SELECT * FROM payment_purchase WHERE purchase_id = :id LIMIT 1")
    suspend fun getById(id: Long): PaymentPurchaseEntity

    @Query("SELECT * FROM payment_purchase WHERE purchase_id = :id")
    fun getAll(id: Long): LiveData<List<PaymentPurchaseEntity>>

    @Query("SELECT * FROM payment_purchase WHERE created_date BETWEEN :startDate AND :finishDate")
    fun getBetweenDates(startDate: Long, finishDate: Long): LiveData<List<PaymentPurchaseEntity>>

    @Query("SELECT * FROM payment_purchase WHERE purchase_id = :id")
    suspend fun getSimpleAll(id: Long): List<PaymentPurchaseEntity>

    /* INSERT */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(item: PaymentPurchaseEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg items: PaymentPurchaseEntity): List<Long>

    /* UPDATE */
    @Update
    suspend fun update(vararg item: PaymentPurchaseEntity): Int
}