package com.titaniocorp.pos.repository.processor

import com.titaniocorp.pos.app.model.Resource
import com.titaniocorp.pos.util.EmptyQueryResultException
import com.titaniocorp.pos.util.getCode
import com.titaniocorp.pos.util.validateCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

/**
 * Peticion a la base de datos manejada todo por Flow
 * @author Juan Ortiz
 * @date 07/05/2020
 */

@ExperimentalCoroutinesApi
@Suppress("UNCHECKED_CAST")
abstract class FlowProcessor<Result, Query> {
    protected abstract fun query(): Flow<Query>
    protected abstract fun validate(response: Result): Int
    protected open suspend fun onResult(response: Result){}

    fun process(): Flow<Resource<Result>> = query()
        .map {
            (it as? Result)?.let {result ->

                val code = validate(result)
                code.validateCode()
                onResult(result)
                Resource.success<Result>(it, code)

            } ?: run { throw EmptyQueryResultException("Query to result failed..") }

        }
        .onStart { emit(Resource.loading<Result>(null)) }
        .catch {exception ->
            with(exception){
                emit(Resource.error<Result>(null, getCode(), message))
            }
        }
        .flowOn(Dispatchers.IO)
}