package com.titaniocorp.pos.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.titaniocorp.pos.app.model.Purchase
import com.titaniocorp.pos.app.model.dto.DetailPurchaseAdapterDto
import com.titaniocorp.pos.app.model.dto.PurchaseDTO

/**
 * Maneja las consultas a la base de datos de pelicula
 * @version 1.0
 * @author Juan Ortiz
 * @date 13/01/2020
 */
@Dao
interface PurchaseDao {
    /* SELECT */
    @Query("SELECT * FROM purchase WHERE purchase_id = :id LIMIT 1")
    suspend fun getById(id: Long): Purchase

    @Query("SELECT * FROM purchase WHERE purchase_id = :id LIMIT 1")
    fun getByIdAsLiveData(id: Long): LiveData<Purchase>

    @Query("SELECT * FROM purchase WHERE active = 1 ORDER BY created_date DESC")
    fun getAll(): LiveData<List<Purchase>>

    @Query("SELECT purchase_id, customer_id, is_credit, total, created_date FROM purchase WHERE active = 1 ORDER BY created_date DESC")
    fun getLightAll(): LiveData<List<PurchaseDTO>>

    @Query("SELECT * FROM purchase WHERE active = 1 AND created_date BETWEEN :startDate AND :finishDate ORDER BY created_date DESC")
    fun getBetweenDates(startDate: Long, finishDate: Long): LiveData<List<Purchase>>

    @Query("SELECT purchase_id, customer_id, is_credit, total, created_date FROM purchase WHERE active = 1 AND created_date BETWEEN :startDate AND :finishDate ORDER BY created_date DESC")
    fun getLightBetweenDates(startDate: Long, finishDate: Long): LiveData<List<PurchaseDTO>>

    @Query("SELECT " +
            "price_purchase.price_purchase_id, " +
            "price.price_id, " +
            "product.name as 'product_name', " +
            "price.name as 'price_name', " +
            "price_purchase.created_date," +
            "price_purchase.refund, " +
            "price_purchase.cost, " +
            "price_purchase.profit, " +
            "price_purchase.tax, " +
            "price_purchase.quantity " +
            "FROM purchase " +
            "INNER JOIN price_purchase ON price_purchase.purchase_id = purchase.purchase_id " +
            "INNER JOIN price ON price.price_id = price_purchase.price_id " +
            "INNER JOIN product ON product.product_id = price.product_id " +
            "WHERE purchase.purchase_id = :id")
    fun getPriceDtoById(id: Long): LiveData<List<DetailPurchaseAdapterDto>>

    @Query("SELECT * FROM purchase WHERE active = 1")
    suspend fun getSimpleAll(): List<Purchase>

    /* INSERT */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(item: Purchase): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg items: Purchase): List<Long>

    /* UPDATE */
    @Update
    suspend fun update(vararg item: Purchase): Int
}