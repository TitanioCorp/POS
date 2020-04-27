package com.titaniocorp.pos.app.viewmodel

import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry

/**
 * Actividad principal de la aplicación
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
open class ObservableViewModel: BaseViewModel(), Observable {
    /* Observable */
    private val mCallbacks: PropertyChangeRegistry = PropertyChangeRegistry()

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) { mCallbacks.add(callback) }
    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) { mCallbacks.remove(callback) }

    fun notifyChange() { mCallbacks.notifyCallbacks(this, 0, null) }
    fun notifyPropertyChanged(fieldId: Int) { mCallbacks.notifyCallbacks(this, fieldId, null) }
}