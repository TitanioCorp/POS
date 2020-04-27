package com.titaniocorp.pos.app.ui.customer

import com.titaniocorp.pos.app.model.Customer
import com.titaniocorp.pos.app.viewmodel.BaseViewModel
import com.titaniocorp.pos.repository.CustomerRepository
import javax.inject.Inject

class DashboardCustomerViewModel @Inject constructor(
    private val customerRepository: CustomerRepository
): BaseViewModel() {
    val customers = customerRepository.getAll()

    fun addCustomer(item: Customer) = customerRepository.add(item)

    fun updateCustomer(item: Customer) = customerRepository.updateProduct(item)

    fun deleteCustomer(position: Int) {
        customers.value?.data?.let {
            customerRepository.updateProduct(it[position].apply { active = false })
        }
    }
}