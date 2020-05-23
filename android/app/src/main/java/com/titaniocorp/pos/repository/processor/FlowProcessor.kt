package com.titaniocorp.pos.repository.processor

import com.titaniocorp.pos.app.model.Resource
import com.titaniocorp.pos.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import timber.log.Timber

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
                Resource.success<Result>(it, code).also {resource ->
                    Timber.tag(Constants.TAG_APP_DEBUG).d("%s: %s", Constants.TAG_DATABASE, resource.toJson())
                }

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