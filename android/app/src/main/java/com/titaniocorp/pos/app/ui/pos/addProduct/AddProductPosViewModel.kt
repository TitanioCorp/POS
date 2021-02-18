package com.titaniocorp.pos.app.ui.pos.addProduct

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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class AddProductPosViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
    private val purchaseRepository: PurchaseRepository,
    private val initialProfitRepository: InitialProfitRepository,
    profitRepository: ProfitRepository
): ObservableViewModel(){
    val profits: LiveData<Resource<List<Profit>>> = profitRepository.getAll()
    private lateinit var initialProfits: List<InitialProfit>

    init {
        viewModelScope.launch {
            initialProfitRepository.getAll().collect {
                it.process({
                    it.data?.let{list -> initialProfits = list }
                })
            }
        }
    }

    @Bindable var product = Product()
    @Bindable var pricePurchase = PricePurchase()
    var profitSelected: Profit = Profit()

    fun setGotProduct(product: Product){
        this.product = product
        notifyPropertyChanged(BR.product)
    }

    fun getProduct(productId: Long) = productRepository.getProductById(productId)

    private fun computePrice(){
        pricePurchase.compute(profitSelected.percent)
        notifyPropertyChanged(BR.pricePurchase)
    }

    fun selectPrice(mPriceId: Long, mCost: Double, mStock: Int, mInitialProfitId: Long){
        with(pricePurchase){
            maxStock = mStock
            priceId = mPriceId
            cost = mCost
            initialProfit = initialProfits.find { it.id == mInitialProfitId }

            computePrice()
        }
    }

    fun addQuantity(){
        with(pricePurchase){
            if((quantity) + 1 <= maxStock){
                quantity += 1
            }
        }
        computePrice()
    }

    fun removeQuantity(){
        with(pricePurchase){
            if((quantity) - 1 >= 1){
                quantity -= 1
            }
        }
        computePrice()
    }

    //region Profit
    fun selectProfit(profit: Profit){
        profitSelected = profit
        computePrice()
    }
    //endregion

    //region Category
    suspend fun getCategory(id: Long): Resource<Category>{
        setLoading(true)
        return categoryRepository.getByIdSingle(id).also {
            setLoading(false)
        }
    }
    //endregion
}