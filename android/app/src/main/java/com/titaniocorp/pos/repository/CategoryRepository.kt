package com.titaniocorp.pos.repository

import androidx.lifecycle.*
import com.titaniocorp.pos.app.model.Category
import com.titaniocorp.pos.app.model.Resource
import com.titaniocorp.pos.database.dao.CategoryDao
import com.titaniocorp.pos.repository.processor.Processor
import com.titaniocorp.pos.repository.processor.SingleProcessor
import com.titaniocorp.pos.util.AppCode
import javax.inject.Inject

/**
 * Maneja todos las ejecuciones con la base de datos o consumo de servicios de peliculas.
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
class CategoryRepository @Inject constructor(private val dao: CategoryDao):  BaseRepository(){

    /* Processor */
    fun getById(id: Long): LiveData<Resource<Category>>{
        return object : Processor<Category, Category>(true){
            override suspend fun query(): Category {
                return  dao.getById(id)
            }
            override fun validate(response: Category): Int {
                return if(response.id ?:0 > 0){
                    AppCode.SUCCESS_QUERY_DATABASE
                }else{
                    AppCode.ERROR_QUERY_DATABASE
                }
            }
        }.asResult()
    }

    fun getAll(): LiveData<Resource<List<Category>>>{
        return object : Processor<List<Category>, LiveData<List<Category>>>(){
            override suspend fun query(): LiveData<List<Category>> = dao.getAll()
            override fun validate(response: List<Category>): Int {
                return if(response.isNotEmpty()){
                    AppCode.SUCCESS_QUERY_DATABASE
                }else{
                    AppCode.ERROR_QUERY_DATABASE
                }
            }
        }.asResult()
    }

    fun add(category: Category): LiveData<Resource<Long>>{
        return object : Processor<Long, Long>(true){
            override suspend fun query(): Long = dao.insert(category)
            override fun validate(response: Long): Int {
                return if(response > 0){
                    AppCode.SUCCESS_QUERY_DATABASE
                }else{
                    AppCode.ERROR_QUERY_DATABASE
                }
            }
        }.asResult()
    }

    fun update(item: Category): LiveData<Resource<Int>>{
        return object : Processor<Int, Int>(true){
            override suspend fun query() = dao.update(item)
            override fun validate(response: Int): Int {
                return if(response > 0){
                    AppCode.SUCCESS_QUERY_DATABASE
                }else{
                    AppCode.ERROR_QUERY_DATABASE
                }
            }
        }.asResult()
    }

    /* Single Processor */
    suspend fun getByIdSingle(id: Long): Resource<Category>{
        return object : SingleProcessor<Category>(){
            override suspend fun query() = dao.getById(id)
            override fun isValidQuery(response: Category): Boolean = (response.id ?:0) > 0
        }.process()
    }
}