package com.titaniocorp.pos.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import org.w3c.dom.Text
import timber.log.Timber
import java.lang.ref.WeakReference
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

fun EditText.addMoneyTextWatcher(callback: ((Double) -> Unit) ?= null){
    addTextChangedListener(MoneyTextWatcher(this, callback))
}

fun Double.formatMoney(): String{
    return MoneyTextWatcher.formatter.format(this)
}

fun String.moneyFilter(): Double{
    return try {
        this.replace(",", "").toDouble()
    }catch (exception: Exception){
        Timber.tag(Constants.TAG_APP_DEBUG).e(exception.localizedMessage)
        0.0
    }
}

fun Double.calculateTax(isInitialProfit: Boolean = true): Double =
    if(isInitialProfit){
        (this / (1 - Configurations.profitPercent)) * Configurations.taxPercent
    }else{
        this * Configurations.taxPercent
    }

class MoneyTextWatcher(editText: EditText, private val callback: ((Double) -> Unit) ?= null): TextWatcher{
    private val editText: WeakReference<EditText> = WeakReference(editText)
    private var isNextDecimal = false

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun afterTextChanged(s: Editable?) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        editText.get()?.removeTextChangedListener(this)

        val cleanString = s.toString().replace(",", "")

        isNextDecimal = calculateDecimal(cleanString)
        val formatString = formatString(cleanString)

        editText.get()?.setText(formatString)
        editText.get()?.setSelection(formatString.length)

        editText.get()?.addTextChangedListener(this)
    }

    private fun calculateDecimal(text: String): Boolean{
        return when{
            text.isEmpty() -> false
            text[text.length - 1].toString() == "." -> true
            else -> isNextDecimal
        }
    }

    private fun formatString(text: String): String{
        return when{
            text.isEmpty() -> {
                callback?.invoke(0.0)
                "0"
            }
            else -> {
                val formatString = text
                    .toDouble()
                    .also{ callback?.invoke(it) }
                    .formatMoney()

                if(isNextDecimal){
                    isNextDecimal = false
                    "${formatString}."
                }else{
                    formatString
                }
            }
        }
    }

    companion object{
        val formatter = DecimalFormat(
            "###,###,###.##",
            DecimalFormatSymbols(Locale.US).apply {
            decimalSeparator = '.'
            groupingSeparator = ','
        })
    }
}
