package com.titaniocorp.pos.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.titaniocorp.pos.database.entity.ProfitEntity

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
    suspend fun getById(id: Long): ProfitEntity

    @Query("SELECT * FROM profit")
    fun getAll(): LiveData<List<ProfitEntity>>

    @Query("SELECT * FROM profit")
    suspend fun getSimpleAll(): List<ProfitEntity>

    /* INSERT */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(item: ProfitEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg items: ProfitEntity): List<Long>

    @Update
    suspend fun update(vararg item: ProfitEntity): Int

    @Delete
    suspend fun delete(item: ProfitEntity): Int
}