package com.titaniocorp.pos.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.titaniocorp.pos.app.model.Stock
import com.titaniocorp.pos.app.model.StockEntity

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
    suspend fun getById(id: Long): StockEntity

    @Query("SELECT * FROM stock")
    fun getAll(): LiveData<List<StockEntity>>

    @Query("SELECT * FROM stock WHERE date BETWEEN :startDate AND :finishDate")
    fun getBetweenDates(startDate: Long, finishDate: Long): LiveData<List<StockEntity>>

    /* INSERT */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(item: StockEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg items: StockEntity): List<Long>

    @Update
    suspend fun update(vararg item: StockEntity): Int
}