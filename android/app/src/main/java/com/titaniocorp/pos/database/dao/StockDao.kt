package com.titaniocorp.pos.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.titaniocorp.pos.app.model.Stock

/**
 * Maneja las consultas a la base de datos de pelicula
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
@Dao
interface StockDao {
    /* SELECT */
    @Query("SELECT * FROM stock WHERE stock_id = :id LIMIT 1")
    suspend fun getById(id: Long): Stock

    @Query("SELECT * FROM stock")
    fun getAll(): LiveData<List<Stock>>

    @Query("SELECT * FROM stock WHERE date BETWEEN :startDate AND :finishDate")
    fun getBetweenDates(startDate: Long, finishDate: Long): LiveData<List<Stock>>

    /* INSERT */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(item: Stock): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg items: Stock): List<Long>

    @Update
    suspend fun update(vararg item: Stock): Int
}