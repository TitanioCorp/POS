package com.titaniocorp.pos.util.ui

import android.app.Activity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.model.*
import com.titaniocorp.pos.app.ui.base.adapter.DialogAddProductAdapter
import com.titaniocorp.pos.databinding.DialogAddAdjustmentBinding
import com.titaniocorp.pos.databinding.DialogAddProductBinding
import com.titaniocorp.pos.databinding.DialogPriceBinding
import com.titaniocorp.pos.util.addMoneyTextWatcher
import com.titaniocorp.pos.util.moneyFilter
import com.titaniocorp.pos.util.validations.ValidateType
import com.titaniocorp.pos.util.validations.ValidateUtil
import com.titaniocorp.pos.util.validations.toValidate

object DialogCustomerHelper {
    fun showCustomer(
        activity: Activity?,
        positiveCallBack: (Customer) -> Unit,
        negativeCallBack: (() -> Unit) ?= null,
        item: Customer ?= null
    ): AlertDialog? = activity?.let {
        val binding: DialogPriceBinding = DataBindingUtil.inflate(
            it.layoutInflater,
            R.layout.dialog_price,
            it.findViewById(android.R.id.content),
            false
        )

        val dialog = MaterialAlertDialogBuilder(activity)
            .setBackground(it.getDrawable(R.drawable.bg_dialog_rounded))
            .setView(binding.root)
            .setTitle("Nuevo cliente")
            .show()


        with(binding){
            item?.let {customer ->
                dialog.setTitle("Actualizar cliente")
                buttonPositive.text = "Actualizar"

                inputName.setText(customer.name)
                inputDni.setText(customer.dni.toString())
                inputPhone.setText(customer.phone.toString())
            }

            clickListener = View.OnClickListener { view ->
                when(view.id){
                    R.id.button_negative -> {
                        dialog.dismiss()
                        negativeCallBack?.invoke()
                    }

                    R.id.button_positive -> {
                        if(ValidateUtil.inputs(
                                inputName.toValidate(),
                                inputDni.toValidate(ValidateType.LONG_NUMBER),
                                inputPhone.toValidate(ValidateType.LONG_NUMBER)
                            )){
                            dialog.dismiss()
                            positiveCallBack(Customer(
                                item?.id ?: 0,
                                inputName.text.toString(),
                                inputDni.text.toString().toLong(),
                                inputPhone.text.toString().toLong()
                            ))
                        }

                    }
                }
            }
        }

        dialog
    }
}