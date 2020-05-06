package com.titaniocorp.pos.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.titaniocorp.pos.app.model.PriceStock
import com.titaniocorp.pos.app.model.PriceStockEntity
import com.titaniocorp.pos.app.model.dto.PriceStockDto

/**
 * Maneja las consultas a la base de datos de pelicula
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
@Dao
interface PriceStockDao {
    /* SELECT */
    @Query("SELECT * FROM price_stock WHERE price_stock_id = :id LIMIT 1")
    suspend fun getById(id: Long): PriceStockEntity

    @Query("SELECT * FROM price_stock WHERE stock_id = :id")
    fun getAllById(id: Long): LiveData<List<PriceStockEntity>>

    @Query("SELECT * FROM price_stock")
    fun getAll(): LiveData<List<PriceStockEntity>>

    //@Query("SELECT * FROM price_stock WHERE stock_id = :id")
    @Query("SELECT " +
            "price_stock.price_stock_id, " +
            "price_stock.stock_id, " +
            "price_stock.price_id, " +
            "price_stock.quantity, " +
            "price_stock.refund, " +
            "product.name as 'product_name', " +
            "price.initial_profit, " +
            "price.cost as 'price_cost', " +
            "price.name as 'price_name' " +
            "FROM price_stock " +
            "INNER JOIN price ON price.price_id = price_stock.price_id " +
            "INNER JOIN product ON product.product_id = price.product_id " +
            "WHERE price_stock.stock_id = :id")
    suspend fun getSimpleAll(id: Long): List<PriceStockDto>

    /* INSERT */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(item: PriceStockEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg items: PriceStockEntity): List<Long>

    @Update
    suspend fun update(vararg item: PriceStockEntity): Int
}