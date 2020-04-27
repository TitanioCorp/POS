package com.titaniocorp.pos.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.titaniocorp.pos.app.model.Customer

/**
 * Maneja las consultas a la base de datos de pelicula
 * @version 1.0
 * @author Juan Ortiz
 * @date 27/12/2019
 */
@Dao
interface CustomerDao {
    /* SELECT */
    @Query("SELECT * FROM customer WHERE customer_id = :id LIMIT 1")
    suspend fun getById(id: Long): Customer

    @Query("SELECT * FROM customer WHERE active = 1")
    fun getAll(): LiveData<List<Customer>>

    @Query("SELECT * FROM customer WHERE active = 1")
    suspend fun getSimpleAll(): List<Customer>

    /* INSERT */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(item: Customer): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg items: Customer): List<Long>

    /* UPDATE */
    @Update
    suspend fun update(vararg item: Customer): Int
}