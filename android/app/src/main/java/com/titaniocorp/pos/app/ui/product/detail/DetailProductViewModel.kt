package com.titaniocorp.pos.app.ui.product.detail

import com.titaniocorp.pos.app.model.Category
import com.titaniocorp.pos.app.model.Product
import com.titaniocorp.pos.repository.CategoryRepository
import com.titaniocorp.pos.repository.ProductRepository
import javax.inject.Inject
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import com.titaniocorp.pos.BR
import com.titaniocorp.pos.app.model.Price
import com.titaniocorp.pos.app.model.Resource
import com.titaniocorp.pos.app.viewmodel.ObservableViewModel
import com.titaniocorp.pos.repository.processor.asLiveEvent
import com.titaniocorp.pos.util.Logger
import com.titaniocorp.pos.util.process
import com.titaniocorp.pos.util.toLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@ExperimentalCoroutinesApi
class DetailProductViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val productRepository: ProductRepository
    ): ObservableViewModel() {

    @Bindable var product = Product()
    @Bindable var categories = listOf<Category>()

    //Price
    fun getPrice(position: Int) = product.prices[position]

    fun addPrice(price: Price){
        if(product.id == 0L){
            product.prices.add(price)
            notifyPropertyChanged(BR.product)
        } else {
            runBlocking{
                price.productId = product.id
                productRepository.insertPrice(price).collect {
                    it.process({
                        Logger.d("Price [${it.data}] inserted on [${product.id}] product!")
                    })
                }
            }
        }
    }

    fun updatePrice(price: Price) = productRepository.updatePrice(price).asLiveEvent()

    fun removePrice(position: Int){
        product.prices[position].let {
            if(it.id > 0){
                it.active = false
                it.sku = ""
            }else{
                product.prices.removeAt(position)
            }
        }
        notifyPropertyChanged(BR.product)
    }

    //Category
    fun getAllCategory() = categoryRepository.getAll().toLiveEvent()
    fun addCategory(category: Category) = categoryRepository.add(category)

    //Product
    fun getProduct(productId: Long) = productRepository.getProductById(productId).toLiveEvent()
    fun addProduct() = productRepository.addProduct(product)
    fun updateProduct() = productRepository.updateProduct(product)
    fun deleteProduct(): LiveData<Resource<Pair<Int, Int>>> {
        with(product) {
            active = false
            prices.forEach {
                it.active = false
                it.sku = ""
            }
        }

        return productRepository.updateProduct(product)
    }
}