package com.titaniocorp.pos.app.model

import android.content.Context
import com.titaniocorp.pos.util.fromJson
import com.titaniocorp.pos.util.getPreferences
import com.titaniocorp.pos.util.toJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Manejo de valores de sesión. Implementa ya el get y set en preferencias compartidas.
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
data class Session(
    val username: String = ""
){
    companion object{
        const val KEY_USER = "KEY_USER"

        /**
         * Obtiene el objeto almacenado como JSON en las preferencias compartidas.
         * @author Juan Ortiz
         * @date 10/09/2019
         * @return Si encuentra valores almacenados en las preferencias combierte el JSON en un
         * objeto, si no encuentra o genera alguna exception entonces va a generar un objeto vacio.
         * */
        fun getInstance(context: Context?): Session{
            return try {
                context!!.let {
                    val preferences = it.getPreferences()
                    val json = preferences.getString(KEY_USER, "") ?: ""
                    (fromJson(json) as Session).also {
                        Timber.d("Session obtenida correctamente")
                    }
                }
            }catch (exception: Exception) {
                Timber.i("Error: ${exception.localizedMessage}")
                Session()
            }
        }
    }
}

/**
 * Extension - Guarda el objeto en las preferencias compartidas.
 * @author Juan Ortiz
 * @date 10/09/2019
 * */
fun Session.save(context: Context?){
    context?.let {
        CoroutineScope(Dispatchers.Default).launch{
            try {
                val preferences = context.getPreferences()
                val json = (this@save as Any).toJson()
                preferences
                    .edit()
                    .putString(Session.KEY_USER, json)
                    .apply()
                Timber.d("Session guardada correctamente")
            }catch (exception: Exception){
                Timber.e(exception)
            }
        }
    }
}