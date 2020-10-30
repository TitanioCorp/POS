package com.titaniocorp.pos.util.ui

import android.app.Activity
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.titaniocorp.pos.R
import com.titaniocorp.pos.databinding.DialogAddAdjustmentBinding
import com.titaniocorp.pos.util.addMoneyTextWatcher
import com.titaniocorp.pos.util.asMoney
import com.titaniocorp.pos.util.getValueMoney
import com.titaniocorp.pos.util.validations.ValidateType
import com.titaniocorp.pos.util.validations.validate

object DialogPOSHelper {
    fun addAdjustment(
        activity: Activity?,
        positiveCallBack: (Double) -> Unit,
        negativeCallBack: (() -> Unit) ?= null,
        total: Double = 0.0
    ): AlertDialog? = activity?.let {
        val binding: DialogAddAdjustmentBinding = DataBindingUtil.inflate(
            it.layoutInflater,
            R.layout.dialog_add_adjustment,
            it.findViewById(android.R.id.content),
            false
        )

        val dialog = MaterialAlertDialogBuilder(activity)
            .setBackground(it.getDrawable(R.drawable.bg_dialog_rounded))
            .setView(binding.root)
            .setTitle("Ingrese el valor")
            .show()


        with(binding){
            currentTotal = total
            adjustment = 0.0
            finalTotal = 0.0

            inputValue.addMoneyTextWatcher{value ->
                val valueAdjustment = value - total

                adjustment = valueAdjustment
                finalTotal = value
            }

            clickListener = View.OnClickListener { view ->
                when(view.id){
                    R.id.button_negative -> {
                        dialog.dismiss()
                        negativeCallBack?.invoke()
                    }

                    R.id.button_positive -> {
                        if(inputValue.validate(ValidateType.MONEY)){
                            dialog.dismiss()
                            val value = inputValue.editableText.toString().getValueMoney()
                            val adjustment = value - total
                            positiveCallBack(adjustment)
                        }
                    }
                }
            }
        }

        dialog
    }
}