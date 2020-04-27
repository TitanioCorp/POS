package com.titaniocorp.pos.app.ui.warehouse

import com.titaniocorp.pos.app.viewmodel.BaseViewModel
import com.titaniocorp.pos.repository.WarehouseRepository
import javax.inject.Inject

class DashboardWarehouseViewModel @Inject constructor(
    repository: WarehouseRepository
): BaseViewModel() {
    val payments = repository.getAll()
}