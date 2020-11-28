package com.titaniocorp.pos.util.ui

import android.app.Activity
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.model.PriceStock
import com.titaniocorp.pos.app.model.Product
import com.titaniocorp.pos.databinding.DialogAddPriceStockBinding
import com.titaniocorp.pos.util.calculateTax
import com.titaniocorp.pos.util.getInitialProfit
import com.titaniocorp.pos.util.validations.ValidateType
import com.titaniocorp.pos.util.validations.validate

object DialogStockHelper {
    fun addPrice(
        activity: Activity?,
        positiveCallBack: (PriceStock) -> Unit,
        negativeCallBack: (() -> Unit) ?= null,
        product: Product,
        priceId: Long
    ): AlertDialog? = activity?.let {
        val binding: DialogAddPriceStockBinding = DataBindingUtil.inflate(
            it.layoutInflater,
            R.layout.dialog_add_price_stock,
            it.findViewById(android.R.id.content),
            false
        )

        val dialog = MaterialAlertDialogBuilder(activity)
            .setCancelable(false)
            .setBackground(it.getDrawable(R.drawable.bg_dialog_rounded))
            .setView(binding.root)
            .setTitle("Añadir stock")
            .show()

        val selectedPrice = product.prices.find {price -> price.id == priceId }

        with(binding){
            price = selectedPrice
            productName = product.name

            val initialProfitPercent = selectedPrice?.initialProfitId?.getInitialProfit()?.percent
            val tax: Double = selectedPrice?.cost?.calculateTax(initialProfitPercent) ?: 0.0
            this.tax = tax

            clickListener = View.OnClickListener { view ->
                when(view.id){
                    R.id.button_negative -> {
                        dialog.dismiss()
                        negativeCallBack?.invoke()
                    }

                    R.id.button_positive -> {
                        inputValue.validate(ValidateType.NUMBER){
                            val quantity = inputValue.text.toString().toInt()

                            val priceStockDto = PriceStock(
                                0,
                                0,
                                selectedPrice?.id ?: 0,
                                quantity,
                                false,
                                selectedPrice?.isInitialProfit ?: false,
                                selectedPrice?.cost ?: 0.0,
                                tax,
                                product.name,
                                selectedPrice?.name ?: "",
                                selectedPrice?.initialProfitId ?: 1
                                )

                            positiveCallBack(priceStockDto)
                            dialog.dismiss()
                        }
                    }
                }
            }
        }
        dialog
    }
}