package com.titaniocorp.pos.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.titaniocorp.pos.database.dao.*
import com.titaniocorp.pos.database.entity.*

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
        InitialProfitEntity::class,
        PurchaseEntity::class,
        PaymentPurchaseEntity::class,
        PricePurchaseEntity::class,
        BillingEntity::class,
        CustomerEntity::class,
        StockEntity::class,
        PriceStockEntity::class,
        PaymentEntity::class,
        PaymentCategoryEntity::class
    ],
    version = 3,
    exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun productDao(): ProductDao
    abstract fun priceDao(): PriceDao
    abstract fun profitDao(): ProfitDao
    abstract fun initialProfitDao(): InitialProfitDao
    abstract fun customerDao(): CustomerDao
    abstract fun purchaseDao(): PurchaseDao
    abstract fun paymentPurchaseDao(): PaymentPurchaseDao
    abstract fun pricePurchaseDao(): PricePurchaseDao
    abstract fun billingDao(): BillingDao
    abstract fun stockDao(): StockDao
    abstract fun priceStockDao(): PriceStockDao
    abstract fun paymentDao(): PaymentDao
    abstract fun reportDao(): ReportDao

    companion object{
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE UNIQUE INDEX `index_product_created_date` ON `product` (`created_date`)")
                database.execSQL("CREATE UNIQUE INDEX `index_price_created_date` ON `price` (`created_date`)")
                database.execSQL("CREATE UNIQUE INDEX `index_purchase_created_date` ON `purchase` (`created_date`)")
                database.execSQL("CREATE UNIQUE INDEX `index_payment_purchase_created_date` ON `payment_purchase` (`created_date`)")
                database.execSQL("CREATE UNIQUE INDEX `index_price_purchase_created_date` ON `price_purchase` (`created_date`)")
                database.execSQL("CREATE UNIQUE INDEX `index_billing_created_date` ON `billing` (`created_date`)")
                database.execSQL("CREATE UNIQUE INDEX `index_payment_date` ON `payment` (`date`)")
                database.execSQL("CREATE UNIQUE INDEX `index_payment_category_date` ON `payment_category` (`date`)")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `initial_profit`(`initial_profit_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `name` TEXT NOT NULL, `percent` REAL NOT NULL);")

                database.execSQL("ALTER TABLE `price` ADD COLUMN `initial_profit_id` INTEGER DEFAULT 2")

                database.execSQL("INSERT INTO `initial_profit` (`name`, `percent`) VALUES ('Default',0.0);")
                database.execSQL("INSERT INTO `initial_profit` (`name`, `percent`) VALUES ('Kenda',16.7);")

                database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_initial_profit_initial_profit_id` ON `initial_profit` (`initial_profit_id`)")
                database.execSQL("CREATE INDEX IF NOT EXISTS `index_price_initial_profit_id` ON `price` (`initial_profit_id`)")
            }
        }

        fun getMigrations(): MutableList<Migration>{
            return mutableListOf<Migration>().apply {
                add(MIGRATION_1_2)
                add(MIGRATION_2_3)
            }
        }
    }
}