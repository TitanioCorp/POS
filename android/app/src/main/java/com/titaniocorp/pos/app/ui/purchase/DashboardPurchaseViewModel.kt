package com.titaniocorp.pos.app.ui.purchase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import com.titaniocorp.pos.app.model.*
import com.titaniocorp.pos.app.model.domain.Customer
import com.titaniocorp.pos.app.viewmodel.BaseViewModel
import com.titaniocorp.pos.repository.CustomerRepository
import com.titaniocorp.pos.repository.PurchaseRepository
import com.titaniocorp.pos.util.DateUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@ExperimentalCoroutinesApi
class DashboardPurchaseViewModel @Inject constructor(
    private val purchaseRepository: PurchaseRepository,
    customerRepository: CustomerRepository
): BaseViewModel() {
    val customers: LiveData<Resource<List<Customer>>> = Transformations.map(customerRepository.getAll()){ it.asDomainResource{ it.data?.asDomainModel() }}
    private val mSearch = MutableLiveData<SearchPurchase>()
    private val isSearching = AtomicBoolean(false)

    val purchases = Transformations.switchMap(mSearch){search ->
        purchaseRepository.search(search).asLiveData()
    }

    init {
        val todayRange = DateUtil.todayRange()
        search(0, 0 , todayRange.first, todayRange.second)
    }

    private fun search(
        type: Int = mSearch.value?.type ?: 0,
        customerId: Long = mSearch.value?.customerId ?: 0,
        startDate: Long = mSearch.value?.startDate ?: 0,
        finishDate: Long = mSearch.value?.finishDate ?: 0
    ){
        if(isSearching.compareAndSet(false, true)){
            mSearch.value = SearchPurchase(type, customerId, startDate, finishDate)
        }
    }

    fun searchFinished(){
        isSearching.set(false)
    }

    fun filterByDate(startDate: Long, finishDate: Long){
        search(startDate = startDate, finishDate = finishDate)
    }

    fun filterByType(type: Int){
        search(type = type)
    }

    fun filterByCustomer(positionAdapter: Int){
        val customerId = if(positionAdapter > 0){
            customers.value?.data?.get(positionAdapter - 1)?.id ?: 0
        }else{
            0
        }

        search(customerId = customerId)
    }
}