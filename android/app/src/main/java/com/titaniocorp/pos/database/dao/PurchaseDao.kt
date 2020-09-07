package com.titaniocorp.pos.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.titaniocorp.pos.database.entity.PurchaseEntity
import com.titaniocorp.pos.app.model.domain.PurchaseDashboardItem
import com.titaniocorp.pos.app.model.dto.DetailPurchaseAdapterDto
import com.titaniocorp.pos.app.model.dto.PurchaseDTO
import kotlinx.coroutines.flow.Flow

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
    suspend fun getById(id: Long): PurchaseEntity

    @Query("SELECT * FROM purchase WHERE purchase_id = :id LIMIT 1")
    fun getByIdAsLiveData(id: Long): LiveData<PurchaseEntity>

    @Query("SELECT * FROM purchase WHERE active = 1 ORDER BY created_date DESC")
    fun getAll(): LiveData<List<PurchaseEntity>>

    @Query("SELECT purchase_id, customer_id, is_credit, total, created_date FROM purchase WHERE active = 1 ORDER BY created_date DESC")
    fun getLightAll(): LiveData<List<PurchaseDTO>>

    @Query("SELECT * FROM purchase WHERE active = 1 AND created_date BETWEEN :startDate AND :finishDate ORDER BY created_date DESC")
    fun getBetweenDates(startDate: Long, finishDate: Long): LiveData<List<PurchaseEntity>>

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
    suspend fun getSimpleAll(): List<PurchaseEntity>

    /* INSERT */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(item: PurchaseEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg items: PurchaseEntity): List<Long>

    /* UPDATE */
    @Update
    suspend fun update(vararg item: PurchaseEntity): Int

    //Flow
    @Query("SELECT * FROM purchase WHERE active = 1 AND created_date BETWEEN :startDate AND :finishDate ORDER BY created_date DESC")
    fun getBetweenDatesFlow(startDate: Long, finishDate: Long): Flow<List<PurchaseEntity>>

    @Query("SELECT * FROM purchase WHERE purchase_id = :id LIMIT 1")
    fun getByIdFlow(id: Long): Flow<PurchaseEntity>

    @Query("SELECT response.* FROM (SELECT purchase.purchase_id, purchase.total, CASE WHEN (purchase.total - sum_payments) IS NULL THEN purchase.total ELSE (purchase.total - sum_payments) END AS receivable, CASE WHEN customer.customer_id IS NULL then 0 ELSE customer.customer_id END AS customer_id, CASE WHEN customer.name IS NULL then '' ELSE customer.name END AS name, purchase.created_date FROM purchase LEFT JOIN (SELECT purchase_id, SUM(value) as sum_payments FROM payment_purchase GROUP BY purchase_id) payment_purchase ON payment_purchase.purchase_id = purchase.purchase_id LEFT JOIN (SELECT customer_id, name FROM customer GROUP BY customer_id) customer ON customer.customer_id = purchase.customer_id) response WHERE CASE WHEN :customerId = 0 THEN response.customer_id >= 0 ELSE response.customer_id = :customerId END AND CASE WHEN :type = 0 THEN response.receivable >= 0 ELSE response.receivable > 0 END AND CASE WHEN :startDate = 0 AND :finishDate = 0 THEN response.created_date > 0 ELSE response.created_date >= :startDate AND response.created_date <= :finishDate END")
    fun search(type: Int, customerId: Long, startDate: Long, finishDate: Long): Flow<List<PurchaseDashboardItem>>
}