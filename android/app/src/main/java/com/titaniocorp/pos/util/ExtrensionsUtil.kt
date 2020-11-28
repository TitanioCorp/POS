package com.titaniocorp.pos.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.titaniocorp.pos.app.model.Resource
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

/**
 * Utilidades para toda la app
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */

/* JSON */
fun Any.toJson(): String{
    return Gson().toJson(this)
}

inline fun <reified A>fromJson(json: String): A{
    return Gson().fromJson(json, A::class.java)
}

/* PEFERENCES */
fun Context.getPreferences(): SharedPreferences {
    return PreferenceManager.getDefaultSharedPreferences(this)
}

/* RESOURCE */
fun <T> Resource<T>.process(
    onSuccess: () -> Unit,
    onError: (()-> Unit) ?= null,
    onLoading: ((Boolean) -> Unit) ?= null){
    when (this.status) {
        Status.LOADING -> {
            onLoading?.invoke(true)
        }

        Status.SUCCESS -> {
            onLoading?.invoke(false)
            onSuccess()
        }

        Status.ERROR -> {
            onLoading?.invoke(false)
            Timber.tag(Constants.TAG_APP_DEBUG).e("Code: $code - Message: $message")
            onError?.invoke()
        }
    }
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    currentFocus?.let {
        hideKeyboard(it)
    }?: run{
        View(this)
    }
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Date.toFormatString(): String{
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.US)
    return format.format(this)
}

fun Date.toFormatFileString(): String{
    val format = SimpleDateFormat("dd-MM-yyyy", Locale.US)
    return format.format(this)
}