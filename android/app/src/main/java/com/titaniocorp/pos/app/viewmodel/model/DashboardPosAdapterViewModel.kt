package com.titaniocorp.pos.app.viewmodel.model

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.titaniocorp.pos.app.model.PricePurchase
import com.titaniocorp.pos.app.model.Profit
import com.titaniocorp.pos.app.model.dto.DashboardPOSAdapterDto
import com.titaniocorp.pos.util.formatMoney

class DashboardPosAdapterViewModel(data: PricePurchase?): ViewModel(){
    private val item = checkNotNull(data)

    val productName = ObservableField(item.productName)
    val profitName = ObservableField(item.profitName)
    val priceName =  ObservableField(item.priceName)

    val cost = ObservableField(item.getCostTotal())
    val total = ObservableField(item.getTotal())
    val quantity = ObservableField(item.quantity)


}