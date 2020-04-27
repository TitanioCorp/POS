package com.titaniocorp.pos.util.ui

import android.app.Activity
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.model.Profit
import com.titaniocorp.pos.databinding.DialogNewProfitBinding
import com.titaniocorp.pos.util.validations.ValidateType
import com.titaniocorp.pos.util.validations.validate

object DialogProfitHelper {
    fun addProfit(
        activity: Activity?,
        positiveCallBack: (Profit) -> Unit,
        negativeCallBack: (() -> Unit) ?= null,
        item: Profit?= null
    ): AlertDialog? = activity?.let {
        val binding: DialogNewProfitBinding = DataBindingUtil.inflate(
            it.layoutInflater,
            R.layout.dialog_new_profit,
            it.findViewById(android.R.id.content),
            false
        )

        val dialog = MaterialAlertDialogBuilder(activity)
            .setBackground(it.getDrawable(R.drawable.bg_dialog_rounded))
            .setView(binding.root)
            .setTitle("Agregar nueva ganancia")
            .show()


        with(binding){
            item?.let {data ->
                profit = data.copy()

                if(data.percent > 0){
                    inputPercent.setText(data.percent.toString())
                }
            }?: run{
                profit = Profit()
            }

            clickListener = View.OnClickListener { view ->
                when(view.id){
                    R.id.button_negative -> {
                        dialog.dismiss()
                        negativeCallBack?.invoke()
                    }

                    R.id.button_positive -> {
                        inputPercent.validate(ValidateType.PERCENT){
                            dialog.dismiss()

                            profit?.let {profit ->
                                profit.percent = inputPercent.text.toString().toDouble()
                                positiveCallBack(profit)
                            }
                        }
                    }
                }
            }
        }

        dialog
    }
}