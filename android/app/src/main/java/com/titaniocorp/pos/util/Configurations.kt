package com.titaniocorp.pos.util

import android.content.Context
import java.io.File

object Configurations {
    const val PROFIT_PERCENT_PREFERENCE = "PROFIT_PERCENT_PREFERENCE"
    const val TAX_PERCENT_PREFERENCE = "TAX_PERCENT_PREFERENCE"

    var profitPercent: Double = 0.0
        private set

    var taxPercent: Double = 0.0
        private set

    var directory: File ?= null
        private set

    fun init(context: Context?): Boolean{
        context?.let {
            profitPercent = (it.getPreferences().getString(PROFIT_PERCENT_PREFERENCE, "0")?.toDouble() ?: 0.0) / 100
            taxPercent = (it.getPreferences().getString(TAX_PERCENT_PREFERENCE, "0")?.toDouble() ?: 0.0) / 100
            return true
        }
        return false
    }

    fun update(key: String, value: Any){
        when(key){
            PROFIT_PERCENT_PREFERENCE -> {profitPercent = (value as String).toDouble() / 100}
            TAX_PERCENT_PREFERENCE -> {taxPercent = (value as String).toDouble() / 100}
        }
    }

    fun setDirectory(file: File?) {
        directory = file
    }
}