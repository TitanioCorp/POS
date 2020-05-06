package com.titaniocorp.pos.repository

import androidx.lifecycle.*
import com.titaniocorp.pos.app.model.Profit
import com.titaniocorp.pos.app.model.Resource
import com.titaniocorp.pos.app.model.asDatabaseModel
import com.titaniocorp.pos.app.model.asDomainModel
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
class ProfitRepository @Inject constructor(
    private val profitDao: ProfitDao
):  BaseRepository(){

    fun getById(id: Long): LiveData<Resource<Profit>>{
        return object : Processor<Profit, Profit>(true){
            override suspend fun query() = profitDao.getById(id).asDomainModel()
            override fun validate(response: Profit): Int = if(response.id > 0){
                AppCode.SUCCESS_QUERY_DATABASE
            }else{
                AppCode.ERROR_QUERY_DATABASE
            }
        }.asResult()
    }

    fun getAll(): LiveData<Resource<List<Profit>>>{
        return object : Processor<List<Profit>, LiveData<List<Profit>>>(){
            override suspend fun query(): LiveData<List<Profit>> = Transformations.map(profitDao.getAll()){ it.asDomainModel() }
            override fun validate(response: List<Profit>): Int = AppCode.SUCCESS_QUERY_DATABASE
        }.asResult()
    }

    fun add(item: Profit): LiveData<Resource<Long>>{
        return object : Processor<Long, Long>(true){
            override suspend fun query() = profitDao.insert(item.asDatabaseModel())
            override fun validate(response: Long): Int = if(response > 0){
                AppCode.SUCCESS_QUERY_DATABASE
            }else{
                AppCode.ERROR_QUERY_DATABASE
            }
        }.asResult()
    }

    fun update(item: Profit): LiveData<Resource<Int>>{
        return object : Processor<Int, Int>(true){
            override suspend fun query() = profitDao.update(item.asDatabaseModel())
            override fun validate(response: Int): Int = if(response > 0){
                AppCode.SUCCESS_QUERY_DATABASE
            }else{
                AppCode.ERROR_QUERY_DATABASE
            }
        }.asResult()
    }

    fun delete(item: Profit): LiveData<Resource<Int>>{
        return object : Processor<Int, Int>(true){
            override suspend fun query() = profitDao.delete(item.asDatabaseModel())
            override fun validate(response: Int): Int = if(response > 0){
                AppCode.SUCCESS_QUERY_DATABASE
            }else{
                AppCode.ERROR_QUERY_DATABASE
            }
        }.asResult()
    }
}