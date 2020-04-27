package com.titaniocorp.pos.app.viewmodel.model

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.titaniocorp.pos.app.model.PricePurchase
import com.titaniocorp.pos.app.model.Profit
import com.titaniocorp.pos.app.model.dto.DashboardPOSAdapterDto
import com.titaniocorp.pos.util.formatMoney

class DashboardPOSAdapterDtoViewModel(item: DashboardPOSAdapterDto?): ViewModel(){
    private val dto = checkNotNull(item)

    val nameProduct = ObservableField(dto.nameProduct)
    val nameProfit = ObservableField(dto.nameProfit)
    val namePrice =  ObservableField(dto.namePrice)

    val cost = ObservableField(dto.cost + dto.profit + dto.tax)
    val total = ObservableField((dto.cost + dto.profit + dto.tax) * dto.quantity)
    val quantity = ObservableField(dto.quantity)


}