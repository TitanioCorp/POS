package com.titaniocorp.pos.util

inline fun <T: Any, reified A: Annotation> T.findFieldAnnotation(property: String) : A?{
    return try {
        val field = this::class.java.getDeclaredField(property)
        field.getAnnotation(A::class.java)
    }catch (exception: Exception){
        null
    }
}

inline fun <T: Any, reified A: Annotation> T.findMethodAnnotation(property: String) : A?{
    return try {
        val method = this::class.java.getDeclaredMethod(property)
        method.getAnnotation(A::class.java)
    }catch (exception: Exception){
        null
    }
}

inline fun <T: Any, reified A: Annotation> T.findClassAnnotation() : A?{
    return try {
        this::class.java.getAnnotation(A::class.java)
    }catch (exception: Exception){
        null
    }
}