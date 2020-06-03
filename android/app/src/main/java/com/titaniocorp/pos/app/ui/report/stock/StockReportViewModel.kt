package com.titaniocorp.pos.app.ui.report.stock

import androidx.databinding.ObservableDouble
import androidx.databinding.ObservableInt
import androidx.lifecycle.asLiveData
import com.titaniocorp.pos.app.viewmodel.BaseViewModel
import com.titaniocorp.pos.repository.ReportRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class StockReportViewModel @Inject constructor(
    reportRepository: ReportRepository
): BaseViewModel() {
    val prices = reportRepository
        .getPricesForStockReport()
        .asLiveData()

    val stock = ObservableInt()
    val total = ObservableDouble()
    val cost = ObservableDouble()
    val tax = ObservableDouble()

    fun calculateTotal(){
        var stockTotal = 0
        var costTotal = 0.0
        var taxTotal = 0.0

        prices.value?.data?.forEach {
            stockTotal += it.stock
            costTotal += it.cost * it.stock
            taxTotal += it.tax * it.stock
        }

        stock.set(stockTotal)
        cost.set(costTotal)
        tax.set(taxTotal)
        total.set(costTotal + taxTotal)
    }
}