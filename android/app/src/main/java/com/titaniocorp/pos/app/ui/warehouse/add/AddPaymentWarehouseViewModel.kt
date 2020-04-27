package com.titaniocorp.pos.app.ui.warehouse.add

import androidx.databinding.Bindable
import com.titaniocorp.pos.app.model.*
import com.titaniocorp.pos.app.viewmodel.ObservableViewModel
import com.titaniocorp.pos.repository.WarehouseRepository
import javax.inject.Inject

/**
 * Logica para agregar un nuevo pago
 * @author Juan Ortiz
 * @date 03/02/2020
 */
class AddPaymentWarehouseViewModel @Inject constructor(
    private val warehouseRepository: WarehouseRepository
): ObservableViewModel() {
    @Bindable val payment = Payment()

    fun addStock() = warehouseRepository.add(payment)
}