package com.titaniocorp.pos.app.ui.stock

import com.titaniocorp.pos.app.viewmodel.BaseViewModel
import com.titaniocorp.pos.repository.StockRepository
import javax.inject.Inject

class DashboardStockViewModel @Inject constructor(
    stockRepository: StockRepository
): BaseViewModel() {
    val stocks = stockRepository.getAll()
}