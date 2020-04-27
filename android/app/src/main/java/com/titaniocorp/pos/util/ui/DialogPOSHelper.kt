package com.titaniocorp.pos.util.ui

import android.app.Activity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.model.PriceAddProductAdapter
import com.titaniocorp.pos.app.model.PricePurchase
import com.titaniocorp.pos.app.model.Product
import com.titaniocorp.pos.app.model.Profit
import com.titaniocorp.pos.app.ui.base.adapter.DialogAddProductAdapter
import com.titaniocorp.pos.databinding.DialogAddAdjustmentBinding
import com.titaniocorp.pos.databinding.DialogAddProductBinding
import com.titaniocorp.pos.util.addMoneyTextWatcher
import com.titaniocorp.pos.util.moneyFilter

object DialogPOSHelper {
    fun addAdjustment(
        activity: Activity?,
        positiveCallBack: (Double) -> Unit,
        negativeCallBack: (() -> Unit) ?= null,
        item: Double = 0.0
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
            .setTitle("Ajustar valor")
            .show()


        with(binding){
            inputValue.addMoneyTextWatcher()
            inputValue.setText(item.toString().replace("-", ""))

            clickListener = View.OnClickListener { view ->
                when(view.id){
                    R.id.button_negative -> {
                        dialog.dismiss()
                        negativeCallBack?.invoke()
                    }

                    R.id.button_positive -> {
                        val value = when{
                            radioSum.isChecked -> {inputValue.text.toString().moneyFilter()}
                            radioSub.isChecked -> {inputValue.text.toString().moneyFilter() * -1}
                            else -> { 0.0}
                        }

                        if(radioSum.isChecked || radioSub.isChecked){
                            dialog.dismiss()
                            positiveCallBack(value)
                        }else{
                            inputValue.error = "Debes seleccionar una opción"
                        }
                    }
                }
            }
        }

        dialog
    }
}