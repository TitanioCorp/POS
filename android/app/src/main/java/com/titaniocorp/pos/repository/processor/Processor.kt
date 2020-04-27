package com.titaniocorp.pos.repository.processor

import androidx.lifecycle.*
import com.hadilq.liveevent.LiveEvent
import com.titaniocorp.pos.app.model.Resource
import com.titaniocorp.pos.util.*
import com.titaniocorp.pos.util.AppCode.Companion.ERROR_QUERY_DATABASE
import kotlinx.coroutines.*
import timber.log.Timber

/**
 * Consume un servicio y lo almacena en la base de datos
 * @version 1.0
 * @author Juan Ortiz
 * @date 18/12/2019
 */
@Suppress("UNCHECKED_CAST")
abstract class Processor<Result, Query> (isLiveEvent: Boolean = false): CoroutineScope by MainScope(){
    private val result: MediatorLiveData<Resource<Result>> =
        if(isLiveEvent){
            LiveEvent()
        }else{
            MediatorLiveData()
        }

    init {
        result.value = Resource.loading(null)
        launch { process() }
    }

    fun asResult() = result
    fun getData() = result.value

    protected abstract suspend fun query(): Query
    protected abstract fun validate(response: Result): Int

    protected fun setValue(newValue: Resource<Result>) {
        if (result.value != newValue) {
            result.value = newValue
            Timber.tag(Constants.TAG_APP_DEBUG).d("%s: %s", Constants.TAG_DATABASE, newValue.toJson())
        }
    }

    private fun setPostValue(newValue: Resource<Result>) {
        if (result.value != newValue) {
            result.postValue(newValue)
            Timber.tag(Constants.TAG_APP_DEBUG).d("%s: %s", Constants.TAG_DATABASE, newValue.toJson())
        }
    }

    private suspend fun process() {
        try {
            when (val queryResult = query()) {

                //LiveData
                is LiveData<*> -> {
                    result.addSource(queryResult) { data ->
                        setPostValue(processData(data as? Result))
                    }
                }

                //Normal Query
                else -> {
                    val liveData = liveData(Dispatchers.IO) {
                        emit(processData(queryResult as? Result))
                    }
                    result.addSource(liveData) { data ->
                        setValue(data)
                    }

                }
            }
        } catch (exception: Exception) {
            with(exception) {
                Timber.tag(Constants.TAG_APP_DEBUG).e(toString())
                setValue(Resource.error(null, getCode(), localizedMessage))
            }
        }
    }

    private fun processData(response: Result?): Resource<Result> = response?.let {
        try {
            val code = validate(it)
            code.validateCode()

            Resource.success(it, code)
        }catch(exception: Exception){
            with(exception){
                Timber.tag(Constants.TAG_APP_DEBUG).e(toString())
                Resource.error(it, getCode(), localizedMessage)
            }
        }
    }?: run{
        Resource.error(null, 0)
    }
}