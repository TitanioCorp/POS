package com.titaniocorp.pos.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.titaniocorp.pos.app.model.PaymentEntity

/**
 * Maneja las consultas a la base de pagos (payments)
 * @author Juan Ortiz
 * @date 03/02/2020
 */
@Dao
interface PaymentDao {
    /* SELECT */
    @Query("SELECT * FROM payment WHERE payment_id = :id LIMIT 1")
    suspend fun getById(id: Long): PaymentEntity

    @Query("SELECT * FROM payment ORDER BY payment_id DESC")
    fun getAll(): LiveData<List<PaymentEntity>>

    @Query("SELECT * FROM payment WHERE date BETWEEN :startDate AND :finishDate ORDER BY payment_id DESC")
    fun getBetweenDates(startDate: Long, finishDate: Long): LiveData<List<PaymentEntity>>

    /* INSERT */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(item: PaymentEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg items: PaymentEntity): List<Long>

    @Update
    suspend fun update(vararg item: PaymentEntity): Int
}