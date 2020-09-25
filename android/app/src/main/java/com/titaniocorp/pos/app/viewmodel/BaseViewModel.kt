package com.titaniocorp.pos.app.viewmodel

import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import com.titaniocorp.pos.app.model.ErrorResource
import javax.inject.Inject
import javax.inject.Named


/**
 * ViewModel base singleton de toda la aplicación
 * @author Juan Ortiz
 * @date 10/09/2019
 */
open class BaseViewModel @Inject constructor(): ViewModel() {
    @Inject
    @Named("LoadingLiveData")
    lateinit var isLoading: LiveEvent<Boolean>

    @Inject
    @Named("ErrorLiveData")
    lateinit var isError: LiveEvent<ErrorResource>

    fun setLoading(boolean: Boolean){ isLoading.value = boolean }
    private fun postLoading(boolean: Boolean){ isLoading.postValue(boolean) }

    fun setError(code: Int?, message: String? = null){ isError.value = ErrorResource(code, message) }
    fun postError(code: Int?, message: String? = null){ isError.postValue(ErrorResource(code, message)) }
}