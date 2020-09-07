package com.titaniocorp.pos.app.ui.customer

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.titaniocorp.pos.app.model.Resource
import com.titaniocorp.pos.app.model.asDomainResource
import com.titaniocorp.pos.app.model.domain.Customer
import com.titaniocorp.pos.app.model.domain.asDatabaseModel
import com.titaniocorp.pos.app.viewmodel.BaseViewModel
import com.titaniocorp.pos.database.entity.asDomainModel
import com.titaniocorp.pos.repository.CustomerRepository
import javax.inject.Inject

class DashboardCustomerViewModel @Inject constructor(
    private val customerRepository: CustomerRepository
): BaseViewModel() {
    val customers: LiveData<Resource<List<Customer>>> = Transformations.map(customerRepository.getAll()){
        it.asDomainResource{ it.data?.asDomainModel() }
    }

    fun addCustomer(item: Customer) = customerRepository.add(item.asDatabaseModel())

    fun updateCustomer(item: Customer) = customerRepository.updateProduct(item.asDatabaseModel())

    fun deleteCustomer(position: Int) {
        customers.value?.data?.let {
            customerRepository.updateProduct(
                it[position]
                    .asDatabaseModel()
                    .apply { active = false }
            )
        }
    }
}