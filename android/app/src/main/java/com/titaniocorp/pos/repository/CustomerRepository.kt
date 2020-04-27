package com.titaniocorp.pos.repository

import androidx.lifecycle.*
import com.titaniocorp.pos.app.model.Customer
import com.titaniocorp.pos.app.model.Resource
import com.titaniocorp.pos.database.dao.CustomerDao
import com.titaniocorp.pos.repository.processor.*
import com.titaniocorp.pos.util.AppCode
import javax.inject.Inject

/**
 * Maneja todos las ejecuciones con la base de datos de clientes(customer)
 * @author Juan Ortiz
 * @date 09/01/2020
 */
class CustomerRepository @Inject constructor(
    private val customerDao: CustomerDao
):  BaseRepository(){

    fun getById(id: Long): LiveData<Resource<Customer>>{
        return object : Processor<Customer, Customer>(true){
            override suspend fun query() = customerDao.getById(id)
            override fun validate(response: Customer): Int {
                return if(response.id > 0){
                    AppCode.SUCCESS_QUERY_DATABASE
                }else{
                    AppCode.ERROR_QUERY_DATABASE
                }
            }
        }.asResult()
    }

    fun getAll(): LiveData<Resource<List<Customer>>>{
        return object : Processor<List<Customer>, LiveData<List<Customer>>>(){
            override suspend fun query(): LiveData<List<Customer>> = customerDao.getAll()
            override fun validate(response: List<Customer>): Int {
                return if(response.isNotEmpty()){
                    AppCode.SUCCESS_QUERY_DATABASE
                }else{
                    AppCode.ERROR_QUERY_DATABASE
                }
            }
        }.asResult()
    }

    fun add(item: Customer): LiveData<Resource<Long>>{
        return object : Processor<Long, Long>(true){
            override suspend fun query() = customerDao.insert(item)
            override fun validate(response: Long): Int {
                return if(response > 0){
                    AppCode.SUCCESS_QUERY_DATABASE
                }else{
                    AppCode.ERROR_QUERY_DATABASE
                }
            }
        }.asResult()
    }

    fun updateProduct(item: Customer): LiveData<Resource<Int>>{
        return object : Processor<Int, Int>(true){
            override suspend fun query() = customerDao.update(item)
            override fun validate(response: Int): Int {
                return if(response > 0){
                    AppCode.SUCCESS_QUERY_DATABASE
                }else{
                    AppCode.ERROR_QUERY_DATABASE
                }
            }
        }.asResult()
    }
}