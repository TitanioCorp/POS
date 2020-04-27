package com.titaniocorp.pos.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.titaniocorp.pos.app.model.Billing
import com.titaniocorp.pos.app.model.Profit

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
    suspend fun getById(id: Long): Billing

    @Query("SELECT * FROM billing")
    fun getAll(): LiveData<List<Billing>>

    @Query("SELECT * FROM billing")
    suspend fun getSimpleAll(): List<Billing>

    /* INSERT */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(item: Billing): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg items: Billing): List<Long>

    @Update
    suspend fun update(vararg item: Billing): Int

    @Delete
    suspend fun delete(item: Billing): Int
}