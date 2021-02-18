package com.titaniocorp.pos.util.ui

import android.app.Activity
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.titaniocorp.pos.R
import com.titaniocorp.pos.databinding.DialogConfirmBinding
import com.titaniocorp.pos.databinding.DialogLoadingBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Utilitario que maneja los dialogos de la aplicación.
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
class DialogUtil {
    companion object {
        fun default(
            activity: Activity,
            message: String ?= null,
            title: String ?= null,
            positiveString: String ?= null,
            negativeString: String ?= null,
            positiveCallback: (() -> Unit)? = null,
            negativeCallback: (() -> Unit)? = null
        ): AlertDialog {
            return MaterialAlertDialogBuilder(activity).apply{
                background = activity.getDrawable(R.drawable.bg_dialog_rounded)
                setTitle(title ?: activity.getString(R.string.success_title))
                setMessage(message)

                positiveCallback?.let {callback ->
                    setPositiveButton(positiveString ?: activity.getString(R.string.action_accept)){_, _ ->
                        callback()
                    }
                }

                negativeCallback?.let {callback ->
                    setNegativeButton(negativeString ?: activity.getString(R.string.action_cancel)){_, _ ->
                        callback()
                    }
                }
            }.create()
        }

        fun progress(activity: Activity): AlertDialog {
            val viewBinding: DialogLoadingBinding =
                DataBindingUtil.inflate(
                    activity.layoutInflater,
                    R.layout.dialog_loading,
                    activity.findViewById(android.R.id.content),
                    false
                )

            return AlertDialog.Builder(activity, R.style.Dialog_Progress)
               .setView(viewBinding.root)
               .setCancelable(false)
               .create()
               .apply {
                   window?.setLayout(WRAP_CONTENT, WRAP_CONTENT)
               }
        }

        fun bottomConfirm(
            activity: Activity,
            strings: Array<String>,
            positiveCallback: (() -> Unit)?= null,
            negativeCallback: (() -> Unit)?= null): BottomSheetDialog {

            return BottomSheetDialog(activity, R.style.Dialog_Confirm).apply {
                setCancelable(false)
                setContentView(
                    (DataBindingUtil.inflate(
                        activity.layoutInflater,
                        R.layout.dialog_confirm,
                        activity.findViewById(android.R.id.content),
                        false
                    ) as DialogConfirmBinding).apply {
                        clickListener = View.OnClickListener {
                            when(it.id){
                                R.id.button_positive -> {
                                    dismiss()
                                    positiveCallback?.invoke()
                                }
                                R.id.button_negative -> {
                                    dismiss()
                                    negativeCallback?.invoke()
                                }
                            }
                        }

                        try {
                            textMessage.text = strings[0]

                            if(strings.size > 1){
                                textTitle.text = strings[1]
                            }

                            if(strings.size > 2){
                                buttonPositive.text = strings[2]
                                buttonNegative.text = strings[3]
                            }
                        }catch (exception: Exception){
                            textMessage.text = ""
                            buttonPositive.isEnabled = false
                        }
                    }.root
                )
            }
        }
    }
}