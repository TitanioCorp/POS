package com.titaniocorp.pos.repository

import androidx.lifecycle.LiveData
import com.titaniocorp.pos.app.model.*
import com.titaniocorp.pos.database.dao.InitialProfitDao
import com.titaniocorp.pos.database.entity.InitialProfitEntity
import com.titaniocorp.pos.database.entity.asDomainModel
import com.titaniocorp.pos.repository.processor.*
import com.titaniocorp.pos.util.AppCode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 *
 * @author Juan Ortiz
 * @date 10/09/2019
 */
@ExperimentalCoroutinesApi
class InitialProfitRepository @Inject constructor(
    private val initialProfitDao: InitialProfitDao
):  BaseRepository(){
    fun getAll(): Flow<Resource<List<InitialProfit>>> {
        return object: FlowProcessor<List<InitialProfit>, List<InitialProfitEntity>>(){
            override fun query() = initialProfitDao.getAll()
            override suspend fun onResult(response: List<InitialProfitEntity>) = response.asDomainModel()
        }.process()
    }

    fun getById(id: Long): Flow<Resource<InitialProfit>>{
        return object : FlowProcessor<InitialProfit, InitialProfit>(){
            override fun query() = initialProfitDao.getById(id).map { it.asDomainModel() }
            override fun validate(response: InitialProfit): Int = if(response.id > 0) {
                AppCode.SUCCESS_QUERY_DATABASE
            } else {
                AppCode.ERROR_QUERY_DATABASE
            }
        }.process()
    }


    fun insert(item: InitialProfit): Flow<Resource<Long>> {
        return object : SingleFlowProcessor<Long, Long>(){
            override fun query(): Long = initialProfitDao.insert(item.asDatabaseModel())
            override fun validate(response: Long): Int = if(response > 0){
                AppCode.SUCCESS_QUERY_DATABASE
            }else{
                AppCode.ERROR_QUERY_DATABASE
            }
        }.process()
    }

    fun update(item: InitialProfit): LiveData<Resource<Int>> {
        return object : Processor<Int, Int>(true){
            override suspend fun query() = initialProfitDao.update(item.asDatabaseModel())
            override fun validate(response: Int): Int = if(response > 0){
                AppCode.SUCCESS_QUERY_DATABASE
            }else{
                AppCode.ERROR_QUERY_DATABASE
            }
        }.asResult()
    }

    fun delete(item: InitialProfit): LiveData<Resource<Int>> {
        return object : Processor<Int, Int>(true){
            override suspend fun query() = initialProfitDao.delete(item.asDatabaseModel())
            override fun validate(response: Int): Int = if(response > 0){
                AppCode.SUCCESS_QUERY_DATABASE
            }else{
                AppCode.ERROR_QUERY_DATABASE
            }
        }.asResult()
    }
}