package com.titaniocorp.pos.util.ui

import android.content.Context
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.titaniocorp.pos.R

fun Context.showDefaultDialog(
    message: String ?= null,
    title: String ?= getString(R.string.success_title),
    positiveString: String ?= getString(R.string.action_accept),
    negativeString: String ?= getString(R.string.action_cancel),
    positiveCallback: (() -> Unit)? = null,
    negativeCallback: (() -> Unit)? = null
){
    MaterialAlertDialogBuilder(this).apply {
        background = ContextCompat.getDrawable(this@showDefaultDialog, R.drawable.bg_dialog_rounded)
        setTitle(title)
        setMessage(message)

        positiveString?.let{
            setPositiveButton(it){_, _ ->
                positiveCallback?.invoke()
            }
        }

        negativeString?.let{
            setNegativeButton(it){_, _ ->
                negativeCallback?.invoke()
            }
        }
    }
        .create()
        .show()
}