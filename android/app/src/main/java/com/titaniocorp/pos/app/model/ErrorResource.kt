package com.titaniocorp.pos.app.model

/**
 * Manejo de errores
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
data class ErrorResource(
    val code: Int?,
    val message: String? = null
)