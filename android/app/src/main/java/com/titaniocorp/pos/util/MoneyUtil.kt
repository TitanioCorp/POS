package com.titaniocorp.pos.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import timber.log.Timber
import java.lang.ref.WeakReference
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*


class MoneyTextWatcher(
    private val editText: EditText,
    private val callback: ((Double) -> Unit)? = null
):  TextWatcher{
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun afterTextChanged(s: Editable?) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        editText.removeTextChangedListener(this)

        val value = s
            .toString()
            .replace(",", "")
            .replace("..", ".")
            .format()

        callback?.invoke(value.getValueMoney())

        editText.setText(value)
        editText.setSelection(value.length)

        editText.addTextChangedListener(this)
    }

    private fun String.format(): String{
        if(isEmpty()){
            return "0"
        }

        val lastChar = this[length - 1]
        val subString = this.substring(0 , length - 1)

        return when{
            lastChar == '.' && subString.contains(".") -> subString.formatAsMoney()
            lastChar == '.' -> "${formatAsMoney()}."
            contains(".0") && this[length - 1] == '0' -> "${formatAsMoney()}.0"
            else -> formatAsMoney()
        }
    }

    companion object{
        val formatter = DecimalFormat(
            "###,###,###.##",
            DecimalFormatSymbols(Locale.US).apply {
                decimalSeparator = '.'
                groupingSeparator = ','
            }
        )
    }
}

fun EditText.addMoneyTextWatcher(callback: ((Double) -> Unit)? = null){
    addTextChangedListener(MoneyTextWatcher(this, callback))
}

fun Double.asMoney(): String = MoneyTextWatcher.formatter.format(this)
fun String.formatAsMoney(): String = this.toDouble().asMoney()
fun String.getValueMoney(): Double{
    return try {
        this.replace(",", "").toDouble()
    }catch (exception: Exception){
        Timber.tag(Constants.TAG_APP_DEBUG).e(exception.localizedMessage)
        0.0
    }
}

fun Double.calculateTotalReal(initialProfit: Double? = 0.0): Double {
    val percent: Double = initialProfit?.let { 1 - (it/100) } ?: 1.0
    return (this / percent)
}

fun Double.calculateTax(initialProfit: Double? = 0.0): Double =
    this.calculateTotalReal(initialProfit) * Configurations.taxPercent


