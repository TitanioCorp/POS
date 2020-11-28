package com.titaniocorp.pos.app.ui.product.detail.price

import androidx.databinding.Bindable
import androidx.lifecycle.asLiveData
import com.titaniocorp.pos.BR
import com.titaniocorp.pos.app.model.Price
import com.titaniocorp.pos.app.viewmodel.ObservableViewModel
import com.titaniocorp.pos.repository.InitialProfitRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class AddPriceProductViewModel @Inject constructor(
    initialProfitRepository: InitialProfitRepository

): ObservableViewModel() {
    val initialProfits = initialProfitRepository.getAll().asLiveData()

    @Bindable
    var price = Price(0)

    fun updatePrice(price: Price){
        this.price = price
        price.compute()
        notifyPropertyChanged(BR.price)
    }

    fun setCost(cost: Double){
        price.cost = cost
        price.compute()
        notifyPropertyChanged(BR.price)
    }

    fun selectInitialProfit(position: Int){
        initialProfits.value?.data?.get(position)?.let{
            price.initialProfitId = it.id
            price.initialProfitSelected = it
            price.compute()
            notifyPropertyChanged(BR.price)
        }
    }
}