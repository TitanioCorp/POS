package com.titaniocorp.pos.app.ui.report.products.stock

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.titaniocorp.pos.app.model.domain.StockReportItem

class StockReportItemViewModel(data: StockReportItem?): ViewModel(){
    private val item = checkNotNull(data)

    val priceId = ObservableField(item.priceId)
    val productName = ObservableField(item.productName)
    val priceName = ObservableField(item.priceName)
    val cost = ObservableField(item.cost)
    val stock = ObservableField(item.stock)
    val isInitialProfit = ObservableField(item.isInitialProfit)

    val tax = ObservableField(item.tax)
    val total = ObservableField(item.total)

    val isSelected = ObservableBoolean(item.isSelected)

    fun setSelected(){
        if(item.isSelected){
            item.isSelected = false
            isSelected.set(false)
        }else{
            item.isSelected = true
            isSelected.set(true)
        }
    }
}