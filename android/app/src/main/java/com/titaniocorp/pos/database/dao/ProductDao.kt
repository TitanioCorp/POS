package com.titaniocorp.pos.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.titaniocorp.pos.database.entity.ProductEntity
import com.titaniocorp.pos.app.model.dto.SearchProductDTO

/**
 * Maneja las consultas a la base de datos de pelicula
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
@Dao
interface ProductDao {
    /* SELECT */
    @Query("SELECT * FROM product WHERE product_id = :id LIMIT 1")
    suspend fun getById(id: Long): ProductEntity

    @Query("SELECT * FROM product WHERE active = 1")
    fun getAll(): LiveData<List<ProductEntity>>

    //@Query("SELECT product_id, name FROM product WHERE active = 1 AND name LIKE :query")
    @Query("SELECT " +
            "product.product_id, " +
            "product.name as 'product_name', " +
            "price.price_id, " +
            "price.name as 'price_name' " +
            "FROM product " +
            "INNER JOIN price ON price.product_id = product.product_id " +
            "WHERE product.active = 1 AND price.active = 1 " +
            "AND (product.name LIKE :query) " +
            "OR(price.name LIKE :query)")
    fun searchProducts(query: String): LiveData<List<SearchProductDTO>>

    /* INSERT */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(item: ProductEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg items: ProductEntity): List<Long>

    @Update
    suspend fun update(vararg item: ProductEntity): Int
}