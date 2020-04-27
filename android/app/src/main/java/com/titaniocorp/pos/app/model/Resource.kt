package com.titaniocorp.pos.app.model

import com.titaniocorp.pos.util.Status

/**
 * Recurso que se usa para los LiveData y servicios. La idea de este objeto es que maneje los tres
 * estados (LOADING, ERROR, SUCCESS) y asi saber en todo momento en qué estado se encuentra la
 * ejecución
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
data class Resource<out T>(val status: Status, val data: T?, val code: Int?, val message: String?) {
    companion object {
        fun <T> success(data: T?, code: Int? = null, message: String? = null): Resource<T> {
            return Resource(Status.SUCCESS, data, code, message)
        }

        fun <T> error(data: T?, code: Int, msg: String?= null): Resource<T> {
            return Resource(Status.ERROR, data, code, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null, null)
        }
    }
}