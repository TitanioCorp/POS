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
    var product = Price(0)

    fun setCost(cost: Double){
        product.cost = cost
        product.compute()
        notifyPropertyChanged(BR.product)
    }

    fun selectInitialProfit(position: Int){
        initialProfits.value?.data?.get(position)?.let{
            product.initialProfitId = it.id
            product.initialProfitSelected = it
            product.compute()
            notifyPropertyChanged(BR.product)
        }
    }
}