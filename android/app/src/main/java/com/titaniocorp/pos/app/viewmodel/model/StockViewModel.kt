package com.titaniocorp.pos.app.viewmodel.model

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.titaniocorp.pos.app.model.Stock

class StockViewModel(data: Stock?): ViewModel(){
    private val item = checkNotNull(data)

    val id = ObservableField(item.id)
    val purchaseRef = ObservableField(item.purchaseRef)
    val total = ObservableField(item.total)
    val date = ObservableField(item.date)
}