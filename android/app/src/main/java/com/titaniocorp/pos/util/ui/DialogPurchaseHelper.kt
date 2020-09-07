package com.titaniocorp.pos.util.ui

import android.app.Activity
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.model.dto.DetailPurchaseAdapterDto
import com.titaniocorp.pos.databinding.DialogAddPaymentBinding
import com.titaniocorp.pos.databinding.DialogAddRefundBinding
import com.titaniocorp.pos.util.addMoneyTextWatcher
import com.titaniocorp.pos.util.asMoney
import com.titaniocorp.pos.util.getValueMoney
import com.titaniocorp.pos.util.validations.ValidateType
import com.titaniocorp.pos.util.validations.validate

object DialogPurchaseHelper {
    fun addPayment(
        activity: Activity?,
        positiveCallBack: (Double) -> Unit,
        negativeCallBack: (() -> Unit) ?= null,
        missingPayment: Double
    ): AlertDialog? = activity?.let {
        val binding: DialogAddPaymentBinding = DataBindingUtil.inflate(
            it.layoutInflater,
            R.layout.dialog_add_payment,
            it.findViewById(android.R.id.content),
            false
        )

        val dialog = MaterialAlertDialogBuilder(activity)
            .setBackground(it.getDrawable(R.drawable.bg_dialog_rounded))
            .setView(binding.root)
            .setTitle("Añadir abono")
            .show()


        with(binding){
            value = missingPayment.asMoney()
            inputValue.addMoneyTextWatcher()

            clickListener = View.OnClickListener { view ->
                when(view.id){
                    R.id.button_negative -> {
                        dialog.dismiss()
                        negativeCallBack?.invoke()
                    }

                    R.id.button_positive -> {
                        inputValue.validate(ValidateType.MONEY){
                            val receivable = binding.value?.getValueMoney() ?: missingPayment
                            val value = inputValue.text.toString().getValueMoney()

                            if(value > receivable){
                                inputValue.error = "La cantidad ingresada es mayor al faltante por cobrar."
                            }else{
                                dialog.dismiss()
                                positiveCallBack(if(value > missingPayment){ missingPayment }else{ value })
                            }
                        }
                    }
                }
            }
        }
        dialog
    }

    fun addRefund(
        activity: Activity?,
        positiveCallBack: (Long, Int) -> Unit,
        negativeCallBack: (() -> Unit) ?= null,
        data: DetailPurchaseAdapterDto
    ): AlertDialog? = activity?.let {
        val binding: DialogAddRefundBinding = DataBindingUtil.inflate(
            it.layoutInflater,
            R.layout.dialog_add_refund,
            it.findViewById(android.R.id.content),
            false
        )

        val dialog = MaterialAlertDialogBuilder(activity)
            .setBackground(it.getDrawable(R.drawable.bg_dialog_rounded))
            .setView(binding.root)
            .setTitle("Añadir reembolso")
            .show()


        with(binding){
            this.item = data
            inputValue.addMoneyTextWatcher()

            clickListener = View.OnClickListener { view ->
                when(view.id){
                    R.id.button_negative -> {
                        dialog.dismiss()
                        negativeCallBack?.invoke()
                    }

                    R.id.button_positive -> {
                        inputValue.validate(ValidateType.NUMBER){
                            val value = inputValue.text.toString().toInt()

                            if(value > data.quantity){
                                inputValue.error = "La cantidad ingresada es mayor al disponible a reembolsar."
                            }else{
                                dialog.dismiss()
                                positiveCallBack(data.pricePaymentId, value)
                            }
                        }
                    }
                }
            }
        }
        dialog
    }
}