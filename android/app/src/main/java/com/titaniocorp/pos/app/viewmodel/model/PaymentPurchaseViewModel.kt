package com.titaniocorp.pos.app.viewmodel.model

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.titaniocorp.pos.app.model.PaymentPurchase
import com.titaniocorp.pos.util.formatMoney
import com.titaniocorp.pos.util.toFormatString

class PaymentPurchaseViewModel(data: PaymentPurchase?): ViewModel(){
    private val item = checkNotNull(data)

    val date = ObservableField(item.createdDate.toFormatString())
    val value = ObservableField(item.value.formatMoney())
}