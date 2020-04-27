package com.titaniocorp.pos.repository

import androidx.lifecycle.*
import com.titaniocorp.pos.app.model.Payment
import com.titaniocorp.pos.app.model.Resource
import com.titaniocorp.pos.database.dao.*
import com.titaniocorp.pos.repository.processor.*
import com.titaniocorp.pos.util.AppCode
import javax.inject.Inject

/**
 * Maneja todos las ejecuciones con la base de datos de Stock.
 * @author Juan Ortiz
 * @date 29/01/2020
 */
class WarehouseRepository @Inject constructor(
    private val dao: PaymentDao
):  BaseRepository(){
    fun getById(id: Long): LiveData<Resource<Payment>>{
        return object : Processor<Payment, Payment>(){
            override suspend fun query(): Payment = dao.getById(id)

            override fun validate(response: Payment): Int {
                return if(response.id > 0){
                    AppCode.SUCCESS_QUERY_DATABASE
                }else{
                    AppCode.ERROR_QUERY_DATABASE
                }
            }
        }.asResult()
    }

    fun getAll(): LiveData<Resource<List<Payment>>>{
        return object : Processor<List<Payment>, LiveData<List<Payment>>>(){
            override suspend fun query(): LiveData<List<Payment>> = dao.getAll()
            override fun validate(response: List<Payment>): Int {
                return if(response.isNotEmpty()){
                    AppCode.SUCCESS_QUERY_DATABASE
                }else{
                    AppCode.ERROR_QUERY_DATABASE
                }
            }
        }.asResult()
    }

    fun add(item: Payment): LiveData<Resource<Pair<Long, Int>>>{
        return object : Processor<Pair<Long, Int>, Pair<Long, Int>>(true){
            override suspend fun query(): Pair<Long, Int> {
                val id = dao.insert(item)

                val code = if(id <= 0){
                    AppCode.ERROR_QUERY_DATABASE
                }else{
                    AppCode.SUCCESS_QUERY_DATABASE
                }

                return Pair(id, code)
            }

            override fun validate(response:Pair<Long, Int>): Int = response.second
        }.asResult()
    }

    fun getBetweenDates(startDate: Long, finishDate: Long): LiveData<Resource<List<Payment>>>{
        return object : Processor<List<Payment>, LiveData<List<Payment>>>(){
            override suspend fun query():  LiveData<List<Payment>> = dao.getBetweenDates(startDate, finishDate)
            override fun validate(response: List<Payment>): Int = if(response.isNotEmpty()){
                AppCode.SUCCESS_QUERY_DATABASE

            }else{
                AppCode.ERROR_QUERY_DATABASE
            }
        }.asResult()
    }
}