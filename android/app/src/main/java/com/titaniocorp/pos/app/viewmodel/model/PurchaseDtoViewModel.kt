package com.titaniocorp.pos.app.viewmodel.model

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.titaniocorp.pos.app.model.Price
import com.titaniocorp.pos.app.model.dto.PurchaseDTO
import com.titaniocorp.pos.util.formatMoney
import com.titaniocorp.pos.util.toFormatString

class PurchaseDtoViewModel(item: PurchaseDTO?): ViewModel(){
    private val data = checkNotNull(item)

    val purchaseId = ObservableField(data.id)
    val customerId = ObservableField(data.customerId)
    val isCredit = ObservableField(data.isCredit)
    val total = ObservableField(data.total.formatMoney())
    val date = ObservableField(data.date.toFormatString())
}