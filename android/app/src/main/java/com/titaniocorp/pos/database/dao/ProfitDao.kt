package com.titaniocorp.pos.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.titaniocorp.pos.app.model.Profit

/**
 * Maneja las consultas a la base de datos de pelicula
 * @version 1.0
 * @author Juan Ortiz
 * @date 27/12/2019
 */
@Dao
interface ProfitDao {
    /* SELECT */
    @Query("SELECT * FROM profit WHERE profit_id = :id LIMIT 1")
    suspend fun getById(id: Long): Profit

    @Query("SELECT * FROM profit")
    fun getAll(): LiveData<List<Profit>>

    @Query("SELECT * FROM profit")
    suspend fun getSimpleAll(): List<Profit>

    /* INSERT */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(item: Profit): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg items: Profit): List<Long>

    @Update
    suspend fun update(vararg item: Profit): Int

    @Delete
    suspend fun delete(item: Profit): Int
}