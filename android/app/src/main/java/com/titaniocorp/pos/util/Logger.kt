package com.titaniocorp.pos.util

import timber.log.Timber

object Logger {
    private const val LOGGER_TAG = "Logger"
    fun d(message: String, tag: String = LOGGER_TAG){
        Timber.tag(tag).d(message)
    }
}