package com.titaniocorp.pos.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.titaniocorp.pos.database.entity.CustomerEntity

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
    suspend fun getById(id: Long): CustomerEntity

    @Query("SELECT * FROM customer WHERE active = 1")
    fun getAll(): LiveData<List<CustomerEntity>>

    @Query("SELECT * FROM customer WHERE active = 1")
    suspend fun getSimpleAll(): List<CustomerEntity>

    /* INSERT */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(item: CustomerEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg items: CustomerEntity): List<Long>

    /* UPDATE */
    @Update
    suspend fun update(vararg item: CustomerEntity): Int
}