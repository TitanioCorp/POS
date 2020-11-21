package com.titaniocorp.pos.repository.processor

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.titaniocorp.pos.app.model.Resource
import com.titaniocorp.pos.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Peticion a la base de datos manejada todo por Flow
 * @author Juan Ortiz
 * @date 07/05/2020
 */

@Suppress("UNCHECKED_CAST")
@ExperimentalCoroutinesApi
abstract class SingleFlowProcessor<Result, Query> {
    protected abstract fun query(): Query
    protected open fun validate(response: Query): Int = AppCode.SUCCESS_QUERY_DATABASE
    protected open suspend fun onResult(response: Query): Result {
        (response as? Result)?.let {result ->
            return result
        } ?: run {
            throw EmptyQueryResultException("Query cannot be cast to Result type!")
        }
    }

    fun process()=
        flow<Resource<Result>> {
            val query = query()

            validate(query).let{code ->
                if(code > 0) {
                    val result = onResult(query)

                    Resource.success<Result>(result, 0).also {resource ->
                        Logger.d("[${Constants.TAG_DATABASE}]: ${resource?.toJson()}")
                    }
                } else {
                    Resource.error<Result>(null, code)
                }
            }
        }.onStart {
            emit(Resource.loading<Result>(null))
        }.catch {exception ->
            with(exception){
                emit(Resource.error<Result>(null, getCode(), message))
            }
        }.flowOn(Dispatchers.IO)
}

@JvmOverloads


fun <T> Flow<T>.asLiveEvent(
    context: CoroutineContext = EmptyCoroutineContext,
    timeoutInMs: Long = 5000L
): LiveData<T> = asLiveData(context, timeoutInMs).toLiveEvent()