package com.titaniocorp.pos.app.viewmodel.model

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.titaniocorp.pos.app.model.Price
import com.titaniocorp.pos.app.model.domain.PurchaseDashboardItem
import com.titaniocorp.pos.app.model.dto.PurchaseDTO
import com.titaniocorp.pos.util.formatMoney
import com.titaniocorp.pos.util.toFormatString

class PurchaseDtoViewModel(data: PurchaseDashboardItem?): ViewModel(){
    private val item = checkNotNull(data)

    val purchaseId = ObservableField(item.purchaseId)
    val customerId = ObservableField(item.customerId)
    val total = ObservableField(item.total)
    val receivable = ObservableField(item.receivable)
    val nameCustomer = ObservableField(item.name)
    val date = ObservableField(item.createdDate.toFormatString())
}