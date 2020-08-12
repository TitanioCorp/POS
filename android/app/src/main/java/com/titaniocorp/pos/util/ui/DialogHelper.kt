package com.titaniocorp.pos.util.ui

import android.app.Activity
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.model.*
import com.titaniocorp.pos.app.model.domain.Customer
import com.titaniocorp.pos.databinding.DialogNewCategoryBinding
import com.titaniocorp.pos.databinding.DialogNewPriceBinding
import com.titaniocorp.pos.util.*
import com.titaniocorp.pos.util.validations.ValidateType
import com.titaniocorp.pos.util.validations.ValidateUtil
import com.titaniocorp.pos.util.validations.toValidate

object DialogHelper {
    fun normal(
        activity: Activity?,
        message: String ?= null,
        title: String ?= null,
        positiveString: String ?= null,
        negativeString: String ?= null,
        positiveCallback: (() -> Unit)? = null,
        negativeCallback: (() -> Unit)? = null
    ): AlertDialog? = activity?.let {
        return MaterialAlertDialogBuilder(activity)
            .apply {
                background = it.getDrawable(R.drawable.bg_dialog_rounded)
                setTitle(title ?: it.getString(R.string.success_title))
                setMessage(message)

                positiveCallback?.let {callback ->
                    setPositiveButton(positiveString ?: it.getString(R.string.action_accept)){_, _ ->
                        callback()
                    }
                }

                negativeCallback?.let {callback ->
                    setNegativeButton(negativeString ?: it.getString(R.string.action_cancel)){_, _ ->
                        callback()
                    }
                }
            }
            .create()
    }

    fun addAdjustment(
        activity: Activity?,
        positiveCallBack: ((Double)) -> Unit,
        negativeCallBack: (() -> Unit) ?= null,
        item: Double = 0.0
    ): AlertDialog? = DialogPOSHelper.addAdjustment(activity, positiveCallBack, negativeCallBack, item)

    fun newProfit(
        activity: Activity?,
        positiveCallBack: (Profit) -> Unit,
        negativeCallBack: (() -> Unit) ?= null,
        item: Profit ?= null
    ): AlertDialog? = DialogProfitHelper.addProfit(activity, positiveCallBack, negativeCallBack, item)

    fun newCategory(
        activity: Activity?,
        positiveCallBack: (Category) -> Unit,
        negativeCallBack: (() -> Unit) ?= null,
        item: Category ?= null): AlertDialog? = activity?.let {
        val binding: DialogNewCategoryBinding = DataBindingUtil.inflate(
            it.layoutInflater,
            R.layout.dialog_new_category,
            it.findViewById(android.R.id.content),
            false
        )

        val dialog = MaterialAlertDialogBuilder(activity)
            .setBackground(it.getDrawable(R.drawable.bg_dialog_rounded))
            .setView(binding.root)
            .setTitle("Crear nueva categoría")
            .show()

        with(binding){
            item?.let {data ->
                category = data.copy()
            }?: run{
                category = Category()
            }

            clickListener = View.OnClickListener { view ->
                when(view.id){
                    R.id.button_negative -> {
                        dialog.dismiss()
                        negativeCallBack?.invoke()
                    }

                    R.id.button_positive -> {
                        if(ValidateUtil.inputs(
                                binding.inputName.toValidate()
                            )){
                            dialog.dismiss()

                            category?.let {category ->
                                positiveCallBack(category)
                            }
                        }
                    }
                }
            }
        }

        dialog
    }

    fun showPrice(
        activity: Activity?,
        positiveCallBack: (Price) -> Unit,
        negativeCallBack: (() -> Unit) ?= null,
        price: Price ?= null
    ): AlertDialog? = activity?.let {
        val binding: DialogNewPriceBinding = DataBindingUtil.inflate(
            it.layoutInflater,
            R.layout.dialog_new_price,
            it.findViewById(android.R.id.content),
            false
        )

        val dialog = MaterialAlertDialogBuilder(activity)
            .setBackground(it.getDrawable(R.drawable.bg_dialog_rounded))
            .setView(binding.root)
            .setTitle("Nuevo punto de precio")
            .show()


        with(binding){
            switchProfitPercent.text = switchProfitPercent.context.getString(
                R.string.text_new_price_init_profit,
                (Configurations.profitPercent * 100).toString()
            )
            switchProfitPercent.isChecked = true

            price?.let {
                inputName.setText(price.name)
                inputSku.setText(price.sku)
                inputCost.setText(price.cost.formatMoney())
                inputStock.setText(price.stock.toString())

                switchProfitPercent.isChecked = price.isInitialProfit

                val tax: Double = price.cost.calculateTax(price.isInitialProfit)

                val total = price.cost + tax
                textTax.text = root.context.getString(R.string.text_money, tax.formatMoney())
                textTotal.text = root.context.getString(R.string.text_money, total.formatMoney())
            }

            inputCost.addMoneyTextWatcher{cost ->
                val tax: Double = if(switchProfitPercent.isChecked){
                    cost.calculateTax()
                }else{
                    cost * Configurations.taxPercent
                }

                val total = cost + tax
                textTax.text = root.context.getString(R.string.text_money, tax.formatMoney())
                textTotal.text = root.context.getString(R.string.text_money, total.formatMoney())
            }

            switchProfitPercent.setOnCheckedChangeListener{_, isChecked ->
                val cost = inputCost.text.toString().moneyFilter()
                val tax: Double = if(isChecked){
                    cost.calculateTax()
                }else{
                    cost * Configurations.taxPercent
                }

                val total = cost + tax
                textTax.text = root.context.getString(R.string.text_money, tax.formatMoney())
                textTotal.text = root.context.getString(R.string.text_money, total.formatMoney())
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
                                inputSku.toValidate(),
                                inputCost.toValidate(ValidateType.MONEY)
                            )){
                            dialog.dismiss()
                            with(binding){
                                positiveCallBack(Price(
                                    price?.id ?: 0,
                                    price?.productId ?: 0,
                                    inputName.text.toString(),
                                    inputSku.text.toString(),
                                    inputCost.text.toString().moneyFilter(),
                                    inputStock.text.toString().toInt(),
                                    switchProfitPercent.isChecked,
                                    price?.active ?: true)
                                )
                            }

                        }
                    }
                }
            }
        }

        dialog
    }

    fun showCustomer(
        activity: Activity?,
        positiveCallBack: ((Customer)) -> Unit,
        negativeCallBack: (() -> Unit) ?= null,
        item: Customer ?= null
    ): AlertDialog? = DialogCustomerHelper.showCustomer(activity, positiveCallBack, negativeCallBack, item)
}