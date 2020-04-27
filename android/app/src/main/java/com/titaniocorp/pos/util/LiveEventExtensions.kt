package com.titaniocorp.pos.util

import androidx.lifecycle.LiveData
import com.hadilq.liveevent.LiveEvent

/**
 * MutableLiveData que solo devuelve una vez el valor
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
fun <T> LiveData<T>.toLiveEvent(): LiveData<T> {
    val result = LiveEvent<T>()
    result.addSource(this) {
        result.value = it
    }
    return result
}