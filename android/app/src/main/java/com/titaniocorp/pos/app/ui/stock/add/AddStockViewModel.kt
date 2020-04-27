package com.titaniocorp.pos.app.ui.stock.add

import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import com.titaniocorp.pos.BR
import com.titaniocorp.pos.app.model.PriceStock
import com.titaniocorp.pos.app.model.Product
import com.titaniocorp.pos.app.model.Resource
import com.titaniocorp.pos.app.model.Stock
import com.titaniocorp.pos.app.model.dto.SearchProductDTO
import com.titaniocorp.pos.app.viewmodel.ObservableViewModel
import com.titaniocorp.pos.repository.ProductRepository
import com.titaniocorp.pos.repository.StockRepository
import com.titaniocorp.pos.util.AppCode.Companion.ERROR_FORM_VALIDATE
import com.titaniocorp.pos.util.toLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddStockViewModel @Inject constructor(
    private val stockRepository: StockRepository,
    private val productRepository: ProductRepository
): ObservableViewModel() {
    private val mSearchQuery = LiveEvent<String>()
    private val searchedId = MutableLiveData<Long>()

    @Bindable val stock = Stock()
    var searchedPriceId: Long = 0

    val searchedList: LiveData<Resource<List<SearchProductDTO>>> = Transformations.switchMap(mSearchQuery){ query ->
        productRepository.searchByString(query)
    }

    val searchProduct: LiveData<Resource<Product>> = Transformations.switchMap(searchedId){ id ->
        productRepository.getProductById(id)
    }.toLiveEvent()

    //region Search
    fun search(query: String){
        if(query != mSearchQuery.value){
            mSearchQuery.value = query
        }
    }

    fun selectProduct(position: Int){
        searchedList.value?.data?.let {
            if(it.isNotEmpty()){
                searchedId.value = it[position].productId
                searchedPriceId = it[position].priceId
            }
        }
    }
    //endregion

    fun addStock() = stockRepository.add(stock)

    private fun computeStock(){
        viewModelScope.launch(Dispatchers.Default){
            with(stock){
                cost = 0.0
                tax = 0.0
                total = 0.0

                prices.forEach {
                    if(!it.isRefund){
                        cost += it.priceCost * it.quantity
                        tax += it.priceTax * it.quantity
                    }
                }

                total = cost + tax
            }
            notifyPropertyChanged(BR.stock)
        }
    }

    fun addPriceToStock(item: PriceStock): Int{
        stock.prices.add(item)
        computeStock()
        return stock.prices.size
    }

    fun removePriceToStock(position: Int): Boolean{
        stock.prices.removeAt(position)
        computeStock()
        return true
    }

    fun validateStock(): Boolean{
        return when{
            stock.prices.size == 0 -> {
                setError(ERROR_FORM_VALIDATE)
                false

            }
            stock.total <= 0.0 -> {
                setError(ERROR_FORM_VALIDATE)
                false
            }

            else -> {true}
        }
    }
}