package com.titaniocorp.pos.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.titaniocorp.pos.app.model.PricePurchaseEntity

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
    suspend fun getById(id: Long): PricePurchaseEntity

    @Query("SELECT * FROM price_purchase WHERE purchase_id = :id")
    fun getAll(id: Long): LiveData<List<PricePurchaseEntity>>

    @Query("SELECT * FROM price_purchase WHERE purchase_id = :id")
    suspend fun getSimpleAll(id: Long): List<PricePurchaseEntity>

    /* INSERT */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(item: PricePurchaseEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg items: PricePurchaseEntity): List<Long>

    /* UPDATE */
    @Update
    suspend fun update(vararg item: PricePurchaseEntity): Int

    /* DELETE */
    @Delete
    suspend fun delete(vararg item: PricePurchaseEntity): Int
}