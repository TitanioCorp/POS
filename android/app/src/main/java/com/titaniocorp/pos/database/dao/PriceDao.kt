package com.titaniocorp.pos.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.titaniocorp.pos.database.entity.PriceEntity

/**
 * Maneja las consultas a la base de datos de pelicula
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
@Dao
interface PriceDao {
    /* SELECT */
    @Query("SELECT * FROM price WHERE product_id = :productId AND active = 1")
    fun getByProductId(productId: Long): LiveData<List<PriceEntity>>

    @Query("SELECT * FROM price WHERE product_id = :productId AND active = 1")
    suspend fun getAllByProduct(productId: Long): List<PriceEntity>

    @Query("SELECT * FROM price")
    fun getAll(): LiveData<List<PriceEntity>>

    /* INSERT */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(item: PriceEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg items: PriceEntity): List<Long>

    @Query("UPDATE price SET stock = (stock - :quantity) WHERE price_id = :priceId")
    suspend fun removeStock(priceId: Long, quantity: Int): Int

    @Query("UPDATE price SET stock = (stock + :quantity) WHERE price_id = :priceId")
    suspend fun addStock(priceId: Long, quantity: Int): Int

    @Update
    fun update(vararg item: PriceEntity): Int
}