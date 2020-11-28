package com.titaniocorp.pos.util

import android.content.Context
import com.titaniocorp.pos.app.model.InitialProfit
import java.io.File

object Configurations {
    const val PROFIT_PERCENT_PREFERENCE = "PROFIT_PERCENT_PREFERENCE"
    const val TAX_PERCENT_PREFERENCE = "TAX_PERCENT_PREFERENCE"
    const val EMAIL_ADMINISTRATOR_PREFERENCE = "EMAIL_ADMINISTRATOR_PREFERENCE"

    var initialProfits = listOf<InitialProfit>()

    var profitPercent: Double = 0.0
        private set

    var taxPercent: Double = 0.0
        private set

    var emailAdmin: String = ""
        private set

    var directory: File ?= null
        private set

    fun init(context: Context?): Boolean{
        context?.let {
            profitPercent = (it.getPreferences().getString(PROFIT_PERCENT_PREFERENCE, "0")?.toDouble() ?: 0.0) / 100
            taxPercent = (it.getPreferences().getString(TAX_PERCENT_PREFERENCE, "0")?.toDouble() ?: 0.0) / 100
            emailAdmin = it.getPreferences().getString(EMAIL_ADMINISTRATOR_PREFERENCE, "") ?: ""
            return true
        }
        return false
    }

    fun update(key: String, value: Any){
        when(key){
            PROFIT_PERCENT_PREFERENCE -> {profitPercent = (value as String).toDouble() / 100}
            TAX_PERCENT_PREFERENCE -> {taxPercent = (value as String).toDouble() / 100}
            EMAIL_ADMINISTRATOR_PREFERENCE -> {emailAdmin = (value as String)}
        }
    }

    fun setDirectory(file: File?) {
        directory = file
    }
}

fun Long.getInitialProfit(): InitialProfit{
    val id = this
    return with(Configurations.initialProfits){
        if(size > 0){
            find { it.id == id } ?: get(0)
        } else {
            InitialProfit()
        }
    }
}