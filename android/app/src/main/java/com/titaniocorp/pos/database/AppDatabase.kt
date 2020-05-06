package com.titaniocorp.pos.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.titaniocorp.pos.app.model.*
import com.titaniocorp.pos.database.dao.*

/**
 * Base de datos de la APP. Implementar acá los DAO
 * @version 1.0
 * @author Juan Ortiz
 * @date 27/12/2019
 */
@Database(
    entities = [
        CategoryEntity::class,
        ProductEntity::class,
        PriceEntity::class,
        ProfitEntity::class,
        PurchaseEntity::class,
        PaymentPurchaseEntity::class,
        PricePurchaseEntity::class,
        BillingEntity::class,
        CustomerEntity::class,
        StockEntity::class,
        PriceStock::class,
        Payment::class,
        PaymentCategory::class
    ],
    version = 1,
    exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun productDao(): ProductDao
    abstract fun priceDao(): PriceDao
    abstract fun profitDao(): ProfitDao
    abstract fun customerDao(): CustomerDao
    abstract fun purchaseDao(): PurchaseDao
    abstract fun paymentPurchaseDao(): PaymentPurchaseDao
    abstract fun pricePurchaseDao(): PricePurchaseDao
    abstract fun billingDao(): BillingDao
    abstract fun stockDao(): StockDao
    abstract fun priceStockDao(): PriceStockDao
    abstract fun paymentDao(): PaymentDao
}