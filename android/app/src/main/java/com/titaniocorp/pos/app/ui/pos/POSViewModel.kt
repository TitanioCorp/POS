package com.titaniocorp.pos.app.ui.pos

import androidx.databinding.Bindable
import androidx.lifecycle.*
import com.hadilq.liveevent.LiveEvent
import com.titaniocorp.pos.BR
import com.titaniocorp.pos.app.model.*
import com.titaniocorp.pos.app.model.dto.DashboardPOSAdapterDto
import com.titaniocorp.pos.app.model.dto.SearchProductDTO
import com.titaniocorp.pos.app.viewmodel.ObservableViewModel
import com.titaniocorp.pos.repository.*
import com.titaniocorp.pos.util.*
import com.titaniocorp.pos.util.AppCode.Companion.ERROR_PURCHASE_EMPTY_PRICES
import com.titaniocorp.pos.util.AppCode.Companion.ERROR_PURCHASE_NO_CUSTOMER
import javax.inject.Inject

class POSViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
    private val purchaseRepository: PurchaseRepository,
    customerRepository: CustomerRepository,
    profitRepository: ProfitRepository
): ObservableViewModel(){
    //Purchase
    @Bindable var purchase = Purchase()

    //Product
    @Bindable var product = Product()
    private var profitSelected: Profit = Profit()
    private var maxStock = 1
    @Bindable var pricePurchase = PricePurchase()

    //Dashboard
    val adapterUpdated = LiveEvent<Pair<Int, Int>>()
    private val mSearchQuery = LiveEvent<String>()
    private val searchedId = MutableLiveData<Long>()

    val searchedList: LiveData<Resource<List<SearchProductDTO>>> = Transformations.switchMap(mSearchQuery){query ->
        productRepository.searchByString(query)
    }

    val searchProduct: LiveData<Resource<Product>> = Transformations.switchMap(searchedId){ id ->
        productRepository.getProductById(id)
    }.toLiveEvent()

    val profits: LiveData<Resource<List<Profit>>> = profitRepository.getAll()
    val customers: LiveData<Resource<List<CustomerEntity>>> = customerRepository.getAll()

    //region Category
    suspend fun getCategory(id: Long): Resource<Category>{
        setLoading(true)
        return categoryRepository.getByIdSingle(id).also {
            setLoading(false)
        }
    }
    //endregion

    //region Search
    fun search(query: String){
        if(query != mSearchQuery.value){
            mSearchQuery.value = query
        }
    }

    fun selectProduct(position: Int){
        val list = searchedList.value?.data
        list?.let {
            if(it.isNotEmpty()){
                clearPrice()
                searchedId.value = it[position].productId
            }
        }
    }
    //endregion

    //region Purchase
    fun computePurchase(){
        with(purchase){
            cost = 0.0
            profit = 0.0
            tax = 0.0

            prices.forEach {
                cost += it.cost * it.quantity
                profit += it.profit * it.quantity
                tax += it.tax * it.quantity
            }
            subtotal = cost + profit
            total = cost + profit + tax + adjustment
            notifyPropertyChanged(BR.purchase)
        }
    }

    fun addProduct(): Int{
        if(pricePurchase.quantity !=0 ){
            val totalQuantity = product.prices.find { it.id == pricePurchase.priceId }?.stock ?: 0
            var currentQuantity = 0

            purchase.prices.forEach {
                if(it.priceId == pricePurchase.priceId){
                    currentQuantity += it.quantity
                }
            }

            return if(pricePurchase.quantity + currentQuantity > totalQuantity){
                AppCode.ERROR_QUANTITY_PRICE_PURCHASE
            }else{
                with(pricePurchase){
                    productName = product.name
                    priceName = product.prices.find { it.id == priceId }?.name ?: ""
                    profitName = profitSelected.name
                }

                purchase.prices.add(pricePurchase)
                adapterUpdated.value = Pair(1, purchase.prices.size - 1)
                computePurchase()
                AppCode.VALIDATE_SUCCESS
            }
        }else{
            return AppCode.ERROR_ZERO_QUANTITY_PRICE
        }
    }

    fun removeProduct(position: Int){
        purchase.prices.removeAt(position)
        computePurchase()
        adapterUpdated.value = Pair(3, position)
    }

    fun setProductSelected(product: Product){
        this.product = product
        notifyPropertyChanged(BR.product)
        notifyPropertyChanged(BR.pricePurchase)
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

    fun setAdjustment(value: Double){
        purchase.adjustment = value
        computePurchase()
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
    fun addCreditPayment(value: Double){
        purchase.payments.add(
            PaymentPurchase(0, 0, 1, value, true)
        )
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
        pricePurchase = PricePurchase()
        product = Product()

        profitSelected = Profit()
        maxStock = 1

        adapterUpdated.value = Pair(4, 0)

        notifyChange()
    }

    //endregion

    //region Price Purchase
    private fun clearPrice(){
        pricePurchase = PricePurchase()
    }

    fun selectProfit(profit: Profit){
        profitSelected = profit
        computePrice()
    }

    fun selectPrice(priceId: Long, cost: Double, stock: Int, isInitialProfit: Boolean){
        with(pricePurchase){
            this.priceId = priceId

            this.tax = if(isInitialProfit){
                cost.calculateTax()
            }else{
                cost * Configurations.taxPercent
            }

            this.cost = cost


            maxStock = stock
            if(maxStock <= quantity){ quantity = stock }
        }

        computePrice()
    }


    private fun computePrice(){
        val profitPercent = profitSelected.percent/100.0

        product.prices.find { it.id == pricePurchase.priceId }?.let {
            if(it.isInitialProfit){
                pricePurchase.profit = (pricePurchase.cost / (1 - Configurations.profitPercent)) * profitPercent
            }else{
                pricePurchase.profit = pricePurchase.cost * profitPercent
            }
        }

        notifyPropertyChanged(BR.pricePurchase)
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
    //endregion
}