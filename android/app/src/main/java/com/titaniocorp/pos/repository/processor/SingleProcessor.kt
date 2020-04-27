package com.titaniocorp.pos.repository.processor

import com.titaniocorp.pos.app.model.Resource
import com.titaniocorp.pos.util.Constants
import com.titaniocorp.pos.util.QueryDatabaseException
import com.titaniocorp.pos.util.getCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

abstract class SingleProcessor<T>{

    protected abstract suspend fun query(): T
    protected abstract fun isValidQuery(response: T): Boolean

    suspend fun process(): Resource<T> = withContext(Dispatchers.IO){
        try {
            val response = query()
            Timber.tag(Constants.TAG_DATABASE).d(response.toString())

            if(isValidQuery(response)){
                Resource.success(response, 1)
            }else{
                throw QueryDatabaseException("El resultado de la consulta no fue válido")
            }
        } catch (exception: Exception) {
            with(exception) {
                Timber.tag(Constants.TAG_DATABASE).e(localizedMessage)
                Resource.error(null, getCode(), localizedMessage)
            }
        }
    }
}