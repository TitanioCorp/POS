package com.titaniocorp.pos.app.ui.profit.initial.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.model.InitialProfit
import com.titaniocorp.pos.databinding.DialogInitialProfitBinding
import com.titaniocorp.pos.util.validations.ValidateType
import com.titaniocorp.pos.util.validations.ValidateUtil
import com.titaniocorp.pos.util.validations.toValidate

fun Context.showInitialProfitDialog(
    onCallBack: (InitialProfit) -> Unit,
    item: InitialProfit ?= null
){
    val binding: DialogInitialProfitBinding = DataBindingUtil.inflate(
        LayoutInflater.from(this),
        R.layout.dialog_initial_profit,
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
        this.item = (item?.copy()) ?: InitialProfit()

        clickListener = View.OnClickListener { view ->
            when(view.id){
                R.id.button_negative -> {
                    dialog.dismiss()
                }

                R.id.button_positive -> {
                    if(ValidateUtil.inputs(
                        inputName.toValidate(),
                        inputPercent.toValidate(ValidateType.PERCENT)
                    )){
                        dialog.dismiss()

                        this.item?.let {
                            it.percent = inputPercent.text.toString().toDouble()
                            onCallBack(it)
                        }
                    }
                }
            }
        }
    }

    dialog.show()
}