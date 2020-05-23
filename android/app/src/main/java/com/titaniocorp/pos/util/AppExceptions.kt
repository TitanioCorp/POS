package com.titaniocorp.pos.util

import com.titaniocorp.pos.util.AppCode.Companion.ERROR_QUERY_DATABASE
import java.net.UnknownHostException

fun Int.validateCode(){
    when(this){
        ERROR_QUERY_DATABASE -> throw QueryDatabaseException("El resultado de la consulta no fue válido")
    }
}

fun Throwable.getCode(): Int{
    var code = AppCode.DEFAULT

    when(this){
        is UnknownHostException -> { code = AppCode.NO_INTERNET }
        is QueryDatabaseException -> { code = ERROR_QUERY_DATABASE }
    }

    return code
}

class EmptyQueryResultException(message: String): Exception(message)
class QueryDatabaseException(message: String): Exception(message)