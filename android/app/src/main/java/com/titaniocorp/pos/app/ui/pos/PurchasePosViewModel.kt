package com.titaniocorp.pos.app.ui.pos

import androidx.databinding.Bindable
import androidx.lifecycle.*
import com.hadilq.liveevent.LiveEvent
import com.titaniocorp.pos.BR
import com.titaniocorp.pos.app.model.*
import com.titaniocorp.pos.app.model.dto.SearchProductDTO
import com.titaniocorp.pos.app.viewmodel.ObservableViewModel
import com.titaniocorp.pos.database.entity.CustomerEntity
import com.titaniocorp.pos.repository.*
import com.titaniocorp.pos.util.*
import com.titaniocorp.pos.util.AppCode.Companion.ERROR_PURCHASE_EMPTY_PRICES
import com.titaniocorp.pos.util.AppCode.Companion.ERROR_PURCHASE_NO_CUSTOMER
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class PurchasePosViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val purchaseRepository: PurchaseRepository,
    customerRepository: CustomerRepository
): ObservableViewModel(){
    //Purchase
    @Bindable var purchase = Purchase()

    private val mSearchQuery = LiveEvent<String>()
    private val searchedId = MutableLiveData<Long>()

    val adapterUpdated = LiveEvent<Pair<Int, Int>>()

    val customers: LiveData<Resource<List<CustomerEntity>>> = customerRepository.getAll()
    val searchedList: LiveData<Resource<List<SearchProductDTO>>> = Transformations.switchMap(mSearchQuery){query ->
        productRepository.searchByString(query)
    }

    //region Purchase
    fun setAdjustment(value: Double){
        purchase.adjustment = value
        computePurchase()
    }

    private fun computePurchase(){
        purchase.compute()
        notifyPropertyChanged(BR.purchase)
    }

    fun validatePurchase(): Boolean = when{
        purchase.prices.isEmpty() -> {
            setError(ERROR_PURCHASE_EMPTY_PRICES)
            false
        }

        purchase.isCredit && purchase.customerId <= 0 -> {
            setError(ERROR_PURCHASE_NO_CUSTOMER)
            false
        }

        else -> {
            true
        }
    }

    fun addPurchase(): LiveData<Resource<Pair<Long, Int>>>{
        if(!purchase.isCredit){
            purchase.payments.add(
                PaymentPurchase(0, 0, 1, purchase.total, false)
            )
        }
        return purchaseRepository.add(purchase)
    }

    fun resetPurchase(){
        purchase = Purchase()
        adapterUpdated.value = Pair(4, 0)

        notifyChange()
    }
    //endregion

    //region Credit
    @Bindable
    fun getIsCredit(): Boolean {
        return purchase.isCredit
    }

    fun setIsCredit(value: Boolean) {
        if (purchase.isCredit != value) {
            purchase.isCredit = value
            notifyPropertyChanged(BR.isCredit)
        }
    }

    fun addCreditPayment(value: Double){
        purchase.payments.add(
            PaymentPurchase(0, 0, 1, value, true)
        )
    }

    fun selectCustomer(position: Int){
        customers.value?.data?.let{
            if(position == 0){
                purchase.customerId = 0
            }else{
                purchase.customerId = it[position - 1].id
            }
        }
    }
    //endregion

    //region Product
    fun searchProducts(query: String){
        if(query != mSearchQuery.value){
            mSearchQuery.value = query
        }
    }

    fun getProductByIndex(index: Int): SearchProductDTO? = searchedList.value?.data?.get(index)

    fun addProduct(pricePurchase: PricePurchase) {
        purchase.prices.add(pricePurchase)
        adapterUpdated.value = Pair(1, purchase.prices.size - 1)
        computePurchase()
    }

    fun removeProduct(position: Int){
        purchase.prices.removeAt(position)
        adapterUpdated.value = Pair(3, position)
        computePurchase()
    }
    //endregion
}