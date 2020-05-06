package com.titaniocorp.pos.repository

import androidx.lifecycle.*
import com.titaniocorp.pos.app.model.Resource
import com.titaniocorp.pos.app.model.Stock
import com.titaniocorp.pos.app.model.asDatabaseModel
import com.titaniocorp.pos.app.model.asDomainModel
import com.titaniocorp.pos.database.dao.*
import com.titaniocorp.pos.repository.processor.*
import com.titaniocorp.pos.util.AppCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

/**
 * Maneja todos las ejecuciones con la base de datos de Stock.
 * @author Juan Ortiz
 * @date 29/01/2020
 */
class StockRepository @Inject constructor(
    private val stockDao: StockDao,
    private val priceStockDao: PriceStockDao,
    private val priceDao: PriceDao
):  BaseRepository(){
    fun getById(id: Long): LiveData<Resource<Stock>>{
        return object : Processor<Stock, Stock>(){
            override suspend fun query(): Stock {
                val stock = stockDao.getById(id).asDomainModel()

                stock.prices.addAll(priceStockDao.getSimpleAll(stock.id).map {
                    it.toPriceStock()
                })

                return stock
            }
            override fun validate(response: Stock): Int {
                return if(response.id > 0){
                    AppCode.SUCCESS_QUERY_DATABASE
                }else{
                    AppCode.ERROR_QUERY_DATABASE
                }
            }
        }.asResult()
    }

    fun getAll(): LiveData<Resource<List<Stock>>>{
        return object : Processor<List<Stock>, LiveData<List<Stock>>>(){
            override suspend fun query(): LiveData<List<Stock>> = Transformations.map(stockDao.getAll()){ it.asDomainModel() }
            override fun validate(response: List<Stock>): Int {
                runBlocking(Dispatchers.IO){
                    response.forEach {
                        it.prices.addAll(priceStockDao.getSimpleAll(it.id).map {dto ->
                            dto.toPriceStock()
                        })
                    }
                }
                return if(response.isNotEmpty()){
                    AppCode.SUCCESS_QUERY_DATABASE
                }else{
                    AppCode.ERROR_QUERY_DATABASE
                }
            }
        }.asResult()
    }

    fun add(item: Stock): LiveData<Resource<Pair<Long, List<Long>>>>{
        return object : Processor<Pair<Long, List<Long>>, Pair<Long, List<Long>>>(true){
            override suspend fun query(): Pair<Long, List<Long>> {
                val id = stockDao.insert(item.asDatabaseModel())

                item.prices.forEach {
                    it.stockId = id
                }

                val listInserted = priceStockDao.insertAll(*item.prices.asDomainModel().toTypedArray())
                var countUpdatedPrice = 0

                item.prices.forEach {
                    if(priceDao.addStock(it.priceId, it.quantity) > 0)
                        countUpdatedPrice++
                }

                return Pair(id, listInserted)
            }

            override fun validate(response:Pair<Long, List<Long>>): Int = if(response.first > 0 && response.second.isNotEmpty()){
                AppCode.SUCCESS_QUERY_DATABASE
            }else{
                AppCode.ERROR_QUERY_DATABASE
            }
        }.asResult()
    }

    fun getBetweenDates(startDate: Long, finishDate: Long): LiveData<Resource<List<Stock>>>{
        return object : Processor<List<Stock>, LiveData<List<Stock>>>(){
            override suspend fun query():  LiveData<List<Stock>> = Transformations.map(stockDao.getBetweenDates(startDate, finishDate)){ it.asDomainModel() }
            override fun validate(response: List<Stock>): Int = if(response.isNotEmpty()){
                AppCode.SUCCESS_QUERY_DATABASE

            }else{
                AppCode.ERROR_QUERY_DATABASE
            }
        }.asResult()
    }
}