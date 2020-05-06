package com.titaniocorp.pos.repository

import androidx.lifecycle.*
import com.titaniocorp.pos.app.model.*
import com.titaniocorp.pos.app.model.dto.SearchProductDTO
import com.titaniocorp.pos.database.dao.PriceDao
import com.titaniocorp.pos.database.dao.ProductDao
import com.titaniocorp.pos.database.dao.ProfitDao
import com.titaniocorp.pos.repository.processor.*
import com.titaniocorp.pos.util.AppCode
import javax.inject.Inject

/**
 * Maneja todos las ejecuciones con la base de datos o consumo de servicios de peliculas.
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
class ProductRepository @Inject constructor(
    private val productDao: ProductDao,
    private val priceDao: PriceDao,
    private val profitDao: ProfitDao
):  BaseRepository(){

    fun getProductById(productId: Long): LiveData<Resource<Product>>{
        return object : Processor<Product, Product>(true){
            override suspend fun query(): Product {
                return  productDao.getById(productId).asDomainModel().also{
                   it.prices.addAll(priceDao.getAllByProduct(productId).asDomainModel())
                }
            }
            override fun validate(response: Product): Int {
                return if(response.id > 0){
                    AppCode.SUCCESS_QUERY_DATABASE
                }else{
                    AppCode.ERROR_QUERY_DATABASE
                }
            }
        }.asResult()
    }

    fun getPOSProductById(productId: Long): LiveData<Resource<Pair<Product, List<Profit>>>>{
        return object : Processor<Pair<Product, List<Profit>>, Pair<Product, List<Profit>>>(){
            override suspend fun query(): Pair<Product, List<Profit>> {
                val product = productDao.getById(productId).asDomainModel().also{
                    it.prices.addAll(priceDao.getAllByProduct(productId).asDomainModel())
                }

                val profits = profitDao.getSimpleAll()

                return Pair(product, profits)
            }
            override fun validate(response: Pair<Product, List<Profit>>): Int {
                return if(response.first.id > 0){
                    AppCode.SUCCESS_QUERY_DATABASE
                }else{
                    AppCode.ERROR_QUERY_DATABASE
                }
            }
        }.asResult()
    }

    fun getAll(): LiveData<Resource<List<Product>>>{
        return object : Processor<List<Product>, LiveData<List<Product>>>(){
            override suspend fun query(): LiveData<List<Product>> = Transformations.map(productDao.getAll()){ it.asDomainModel() }
            override fun validate(response: List<Product>): Int {
                return if(response.isNotEmpty()){
                    AppCode.SUCCESS_QUERY_DATABASE
                }else{
                    AppCode.ERROR_QUERY_DATABASE
                }
            }
        }.asResult()
    }

    fun addProduct(product: Product): LiveData<Resource<Pair<Long, List<Long>>>>{
        return object : Processor<Pair<Long, List<Long>>, Pair<Long, List<Long>>>(true){
            override suspend fun query(): Pair<Long, List<Long>> {
                val productId = productDao.insert(product.asDatabaseModel())

                product.prices.forEach {
                    it.productId = productId
                }

                val pricesId = priceDao.insertAll(*product.prices.asDatabaseModel().toTypedArray())

                return Pair(productId, pricesId)
            }

            override fun validate(response:Pair<Long, List<Long>>): Int = if(response.first > 0 && response.second.isNotEmpty()){
                AppCode.SUCCESS_QUERY_DATABASE
            }else{
                AppCode.ERROR_QUERY_DATABASE
            }
        }.asResult()
    }

    fun updateProduct(product: Product): LiveData<Resource<Pair<Int, Int>>>{
        return object : Processor<Pair<Int,Int>, Pair<Int, Int>>(true){
            override suspend fun query(): Pair<Int, Int> {
                val productId = productDao.update(product.asDatabaseModel())

                product.prices.forEach {
                    it.productId = product.id
                }

                val pricesId = priceDao.insertAll(*product.prices.asDatabaseModel().toTypedArray())

                return Pair(productId, pricesId.size)
            }

            override fun validate(response:Pair<Int,Int>): Int = if(response.first > 0 && response.second > 0){
                AppCode.SUCCESS_QUERY_DATABASE
            }else{
                AppCode.ERROR_QUERY_DATABASE
            }

        }.asResult()
    }

    fun searchByString(query :String): LiveData<Resource<List<SearchProductDTO>>>{
        return object : Processor<List<SearchProductDTO>, LiveData<List<SearchProductDTO>>>(){
            override suspend fun query():LiveData<List<SearchProductDTO>> {
                return productDao.searchProducts("%$query%")
            }
            override fun validate(response: List<SearchProductDTO>): Int = if(response.isNotEmpty()){
                AppCode.SUCCESS_QUERY_DATABASE
            }else{
                AppCode.ERROR_QUERY_DATABASE
            }
        }.asResult()
    }
}