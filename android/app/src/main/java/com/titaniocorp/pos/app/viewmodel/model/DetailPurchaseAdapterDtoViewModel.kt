package com.titaniocorp.pos.app.viewmodel.model

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.titaniocorp.pos.app.model.dto.DetailPurchaseAdapterDto

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