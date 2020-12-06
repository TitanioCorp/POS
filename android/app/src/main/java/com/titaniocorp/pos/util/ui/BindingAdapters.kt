package com.titaniocorp.pos.util.ui

import android.widget.Button
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText
import com.titaniocorp.pos.R
import com.titaniocorp.pos.util.asMoney
import com.titaniocorp.pos.util.toFormatString
import java.util.*

@BindingAdapter("asMoney")
fun bindAsMoney(textView: TextView, value: Double) {
    with(textView){
        text = context.getString(R.string.text_money, value.asMoney())
    }
}

@BindingAdapter("asTax")
fun bindAsTax(textView: TextView, value: Double) {
    with(textView){
        text = context.getString(R.string.text_binding_tax, value.asMoney())
    }
}

@BindingAdapter("asQuantity")
fun bindAsQuantity(textView: TextView, value: Int) {
    textView.text = textView.context.getString(R.string.text_quantity, value.toString())
}

@BindingAdapter("asDate")
fun bindAsDate(textView: TextView, date: Date) {
    textView.text = date.toFormatString()
}

@BindingAdapter("asCalendar")
fun bindAsCalendar(button: Button, calendar: Calendar) {
    button.text = calendar.time.toFormatString()
}

@BindingAdapter("cost", "tax")
fun bindCalculateCost(textView: TextView, cost: Double, tax: Double) {
    val total = cost + tax
    textView.text = textView.context.getString(R.string.text_money, total.asMoney())
}

@BindingAdapter("cost", "profit", "tax", "quantity")
fun bindTotal(textView: TextView, cost: Double, profit: Double, tax: Double, quantity: Double) {
    val total = (cost + profit + tax) * quantity
    textView.text = textView.context.getString(R.string.text_money, total.asMoney())
}

@BindingAdapter("asPercent")
fun bindAsPercent(textView: TextView, value: Double) {
    textView.text = textView.context.getString(R.string.text_as_percent, value.toString())
}

@BindingAdapter("asInputPercent")
fun bindAsInputPercent(editText: TextInputEditText, value: Double) {
    editText.setText(value.toString())
}

@BindingAdapter("asInputMoney")
fun bindAsInputMoney(editText: TextInputEditText, value: Double) {
    editText.setText(value.asMoney())
}

@BindingAdapter("asTotalProducts")
fun bindAsTotalProducts(textView: TextView, size: Int) {
    textView.text = textView.context.getString(R.string.text_as_total_products, size.toString())
}
