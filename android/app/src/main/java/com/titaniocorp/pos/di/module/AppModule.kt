package com.titaniocorp.pos.di.module

import android.app.Application
import androidx.room.Room
import com.titaniocorp.pos.BuildConfig
import com.titaniocorp.pos.api.API
import com.titaniocorp.pos.database.AppDatabase
import com.titaniocorp.pos.database.dao.*
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import okhttp3.logging.HttpLoggingInterceptor

@Module(includes = [ViewModelModule::class])
class AppModule {
    @Singleton
    @Provides
    fun provideApiService(): API {
        val client = OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if(BuildConfig.DEBUG){
                        HttpLoggingInterceptor.Level.BODY
                    }else{
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            )
            .build()
        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.urlBase)
            .build()
            .create(API::class.java)
    }

    @Singleton
    @Provides
    fun provideDb(app: Application): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, "appdatabase.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideCategoryDao(db: AppDatabase): CategoryDao = db.categoryDao()

    @Singleton
    @Provides
    fun provideProductDao(db: AppDatabase): ProductDao = db.productDao()

    @Singleton
    @Provides
    fun providePriceDao(db: AppDatabase): PriceDao = db.priceDao()

    @Singleton
    @Provides
    fun provideProfitDao(db: AppDatabase): ProfitDao = db.profitDao()

    @Singleton
    @Provides
    fun provideCustomerDao(db: AppDatabase): CustomerDao = db.customerDao()

    @Singleton
    @Provides
    fun providePurchaseDao(db: AppDatabase): PurchaseDao = db.purchaseDao()

    @Singleton
    @Provides
    fun providePaymentPurchaseDao(db: AppDatabase): PaymentPurchaseDao = db.paymentPurchaseDao()

    @Singleton
    @Provides
    fun providePricePurchaseDao(db: AppDatabase): PricePurchaseDao = db.pricePurchaseDao()

    @Singleton
    @Provides
    fun provideBillingDao(db: AppDatabase): BillingDao = db.billingDao()

    @Singleton
    @Provides
    fun provideStockDao(db: AppDatabase): StockDao = db.stockDao()

    @Singleton
    @Provides
    fun providePriceStockDao(db: AppDatabase): PriceStockDao = db.priceStockDao()

    @Singleton
    @Provides
    fun providePaymentDao(db: AppDatabase): PaymentDao = db.paymentDao()
}