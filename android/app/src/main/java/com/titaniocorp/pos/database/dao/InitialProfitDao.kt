package com.titaniocorp.pos.database.dao

import androidx.room.*
import com.titaniocorp.pos.database.entity.InitialProfitEntity
import kotlinx.coroutines.flow.Flow

/**
 * Maneja las consultas a la base de datos de pelicula
 * @version 1.0
 * @author Juan Ortiz
 * @date 27/12/2019
 */
@Dao
interface InitialProfitDao {
    /* SELECT */
    @Query("SELECT * FROM initial_profit WHERE initial_profit_id = :id LIMIT 1")
    fun getById(id: Long): Flow<InitialProfitEntity>

    @Query("SELECT * FROM initial_profit")
    fun getAll(): Flow<List<InitialProfitEntity>>

    /* INSERT */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(item: InitialProfitEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg items: InitialProfitEntity): List<Long>

    @Update
    suspend fun update(vararg item: InitialProfitEntity): Int

    @Delete
    suspend fun delete(item: InitialProfitEntity): Int
}