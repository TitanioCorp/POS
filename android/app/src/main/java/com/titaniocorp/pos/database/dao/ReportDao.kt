package com.titaniocorp.pos.database.dao

import androidx.room.*
import com.titaniocorp.pos.app.model.domain.StockReportItem
import kotlinx.coroutines.flow.Flow

/**
 * Maneja las consultas de los reportes
 * @author Juan Ortiz
 * @date 03/06/2020
 */
@Dao
interface ReportDao {
    @Query("SELECT price.price_id, product.name as 'product_name', price.name as 'price_name', price.cost, price.stock, price.initial_profit FROM price INNER JOIN product ON product.product_id = price.product_id WHERE price.stock > 0 AND price.active = 1")
    fun getPricesForStockReport(): Flow<List<StockReportItem>>
}