package com.titaniocorp.pos.app.viewmodel.model

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.titaniocorp.pos.app.model.PricePurchase

class DashboardPosAdapterViewModel(data: PricePurchase?): ViewModel(){
    private val item = checkNotNull(data)

    val productName = ObservableField(item.productName)
    val profitName = ObservableField(item.profitName)
    val priceName =  ObservableField(item.priceName)

    val cost = ObservableField(item.getCostTotal())
    val total = ObservableField(item.getTotal())
    val quantity = ObservableField(item.quantity)


}