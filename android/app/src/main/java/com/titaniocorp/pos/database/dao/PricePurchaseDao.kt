package com.titaniocorp.pos.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.titaniocorp.pos.app.model.PricePurchase

/**
 * Maneja las consultas a la base de datos de pelicula
 * @version 1.0
 * @author Juan Ortiz
 * @date 27/12/2019
 */
@Dao
interface PricePurchaseDao {
    /* SELECT */
    @Query("SELECT * FROM price_purchase WHERE price_purchase_id = :id LIMIT 1")
    suspend fun getById(id: Long): PricePurchase

    @Query("SELECT * FROM price_purchase WHERE purchase_id = :id")
    fun getAll(id: Long): LiveData<List<PricePurchase>>

    @Query("SELECT * FROM price_purchase WHERE purchase_id = :id")
    suspend fun getSimpleAll(id: Long): List<PricePurchase>

    /* INSERT */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(item: PricePurchase): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg items: PricePurchase): List<Long>

    /* UPDATE */
    @Update
    suspend fun update(vararg item: PricePurchase): Int

    /* DELETE */
    @Delete
    suspend fun delete(vararg item: PricePurchase): Int
}