package com.titaniocorp.pos.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.titaniocorp.pos.database.entity.BillingEntity

/**
 * Maneja las consultas a la base de datos de pelicula
 * @version 1.0
 * @author Juan Ortiz
 * @date 27/12/2019
 */
@Dao
interface BillingDao {
    /* SELECT */
    @Query("SELECT * FROM billing WHERE billing_id = :id LIMIT 1")
    suspend fun getById(id: Long): BillingEntity

    @Query("SELECT * FROM billing")
    fun getAll(): LiveData<List<BillingEntity>>

    @Query("SELECT * FROM billing")
    suspend fun getSimpleAll(): List<BillingEntity>

    /* INSERT */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(item: BillingEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg items: BillingEntity): List<Long>

    @Update
    suspend fun update(vararg item: BillingEntity): Int

    @Delete
    suspend fun delete(item: BillingEntity): Int
}