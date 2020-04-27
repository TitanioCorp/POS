package com.titaniocorp.pos.app.viewmodel.model

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.titaniocorp.pos.app.model.PricePurchase
import com.titaniocorp.pos.app.model.Profit
import com.titaniocorp.pos.app.model.dto.DashboardPOSAdapterDto
import com.titaniocorp.pos.app.model.dto.DetailPurchaseAdapterDto
import com.titaniocorp.pos.util.formatMoney
import com.titaniocorp.pos.util.toFormatString

class DetailPurchaseAdapterDtoViewModel(data: DetailPurchaseAdapterDto?): ViewModel(){
    private val item = checkNotNull(data)

    val nameProduct = ObservableField(item.nameProduct)
    val namePrice =  ObservableField(item.namePrice)

    val date =  ObservableField(item.createdDate)
    val cost = ObservableField(item.cost)
    val profit = ObservableField(item.profit)
    val tax = ObservableField(item.tax)
    val quantity = ObservableField(item.quantity)

}