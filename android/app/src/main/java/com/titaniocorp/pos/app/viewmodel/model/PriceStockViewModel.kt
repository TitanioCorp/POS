package com.titaniocorp.pos.app.viewmodel.model

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.titaniocorp.pos.app.model.PriceStock

class PriceStockViewModel(data: PriceStock?): ViewModel(){
    private val item = checkNotNull(data)

    val id = ObservableField(item.id)
    val productName = ObservableField(item.productName)
    val priceName = ObservableField(item.priceName)
    val quantity = ObservableField(item.quantity)
    val priceCost = ObservableField(item.priceCost + item.priceTax)
    val total = ObservableField((item.priceCost + item.priceTax) * item.quantity)
}