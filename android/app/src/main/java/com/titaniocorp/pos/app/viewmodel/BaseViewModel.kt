package com.titaniocorp.pos.app.viewmodel

import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import com.titaniocorp.pos.app.model.ErrorResource


/**
 * Actividad principal de la aplicación
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
open class BaseViewModel: ViewModel() {
    val isLoading: LiveEvent<Boolean> by lazy { LiveEvent<Boolean>().apply { postValue(false) } }
    val isError: LiveEvent<ErrorResource> by lazy { LiveEvent<ErrorResource>()}

    fun setLoading(boolean: Boolean){ isLoading.value = boolean }
    private fun postLoading(boolean: Boolean){ isLoading.postValue(boolean) }

    fun setError(code: Int?, message: String? = null){ isError.value = ErrorResource(code, message) }
    fun postError(code: Int?, message: String? = null){ isError.postValue(ErrorResource(code, message)) }
}