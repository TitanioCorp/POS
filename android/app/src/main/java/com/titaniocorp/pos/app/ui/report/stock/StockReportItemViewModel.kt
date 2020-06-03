package com.titaniocorp.pos.app.ui.report.stock

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.titaniocorp.pos.app.model.Price
import com.titaniocorp.pos.app.model.domain.PurchaseDashboardItem
import com.titaniocorp.pos.app.model.domain.StockReportItem
import com.titaniocorp.pos.app.model.dto.PurchaseDTO
import com.titaniocorp.pos.util.formatMoney
import com.titaniocorp.pos.util.toFormatString

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
}