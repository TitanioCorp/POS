package com.titaniocorp.pos.app.ui.product.detail.price.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.model.InitialProfit
import com.titaniocorp.pos.databinding.DialogInitialProfitBinding
import com.titaniocorp.pos.databinding.DialogSetCostBinding
import com.titaniocorp.pos.util.DateUtil
import com.titaniocorp.pos.util.addMoneyTextWatcher
import com.titaniocorp.pos.util.formatAsMoney
import com.titaniocorp.pos.util.getValueMoney
import com.titaniocorp.pos.util.validations.ValidateType
import com.titaniocorp.pos.util.validations.ValidateUtil
import com.titaniocorp.pos.util.validations.toValidate

fun Context.showSetCostDialog(
    onCallBack: (Double) -> Unit,
    currentValue: Double = 0.0
){
    val binding: DialogSetCostBinding = DataBindingUtil.inflate(
        LayoutInflater.from(this),
        R.layout.dialog_set_cost,
        null,
        false
    )

    val dialog = MaterialAlertDialogBuilder(this)
        .setBackground(ContextCompat.getDrawable(this, R.drawable.bg_dialog_rounded))
        .setView(binding.root)
        .setTitle(getString(R.string.title_new_initial_profit))
        .setCancelable(false)
        .show()

    with(binding){
        finalCost = 0.0
        percent = 0.0

        val funCalculate: (Double, Double) -> Unit = { cost, percent ->
            finalCost = cost * (1 - (percent/100))
        }

        spinnerPercent.apply {
            adapter = ArrayAdapter(
                binding.root.context,
                android.R.layout.simple_spinner_dropdown_item,
                resources.getStringArray(R.array.percents)
            )

            setSelection(0)

            onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long){
                    val percent = when(position){
                        0 -> { 0.0 }
                        1 -> { 5.0 }
                        2 -> { 10.0 }
                        3 -> { 15.0 }
                        4 -> { 20.0 }
                        else -> { 0.0 }
                    }
                    binding.percent = percent
                    val inputValue = inputCost.text.toString().getValueMoney()
                    funCalculate(inputValue, percent)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }

        inputCost.addMoneyTextWatcher{cost ->
            funCalculate(cost, percent ?: 0.0)
        }

        clickListener = View.OnClickListener { view ->
            when(view.id){
                R.id.button_negative -> {
                    dialog.dismiss()
                }

                R.id.button_positive -> {
                    if(ValidateUtil.inputs(
                        inputCost.toValidate(ValidateType.MONEY)
                    )){
                        dialog.dismiss()
                        onCallBack(finalCost ?: 0.0)
                    }
                }
            }
        }
    }

    dialog.show()
}