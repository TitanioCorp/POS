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
import com.titaniocorp.pos.BR
import com.titaniocorp.pos.app.model.Price
import com.titaniocorp.pos.app.model.Resource
import com.titaniocorp.pos.app.viewmodel.ObservableViewModel

class DetailProductViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val productRepository: ProductRepository
    ): ObservableViewModel() {

    @Bindable
    var product = Product()

    @Bindable
    var categories = listOf<Category>()

    val mProductId = MutableLiveData<Long>()

    fun getPrice(position: Int) = product.prices[position]
    fun updatePrice(position: Int, price: Price) {
        product.prices[position] = price
        notifyPropertyChanged(BR.product)
    }

    fun addPrice(price: Price){
        product.prices.add(price)
        notifyPropertyChanged(BR.product)
    }

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

    fun getAllCategory() = categoryRepository.getAll()
    fun addCategory(category: Category) = categoryRepository.add(category)

    fun getProduct() = Transformations.switchMap(mProductId){ productId ->
        productRepository.getProductById(productId)
    }
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