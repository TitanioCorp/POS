package com.titaniocorp.pos.app.ui.product

import androidx.lifecycle.*
import com.titaniocorp.pos.app.model.Product
import com.titaniocorp.pos.app.viewmodel.ObservableViewModel
import com.titaniocorp.pos.repository.ProductRepository
import com.titaniocorp.pos.util.process
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DashboardProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
    ): ObservableViewModel() {

    private val mProducts = MediatorLiveData<List<Product>>()
    val products: LiveData<List<Product>> = mProducts

    private val allProducts = productRepository.getAll()

    init {
        mProducts.addSource(allProducts){resource ->
            resource.process(
                onLoading = {boolean -> setLoading(boolean)},
                onSuccess = {
                    resource.data?.let {
                        if(it.isNotEmpty()){
                            mProducts.value = it
                        }else{
                            setError(resource.code, resource.message)
                        }
                    }
                }
            )
        }
    }

    fun filter(query: String){
        viewModelScope.launch{
            withContext(Dispatchers.Default){
                allProducts.value?.data?.let{
                    val list = it.filter{item -> item.name.contains(query,true)}

                    if(list.isNotEmpty()){
                        mProducts.postValue(list)
                    }else{
                        mProducts.postValue(it)
                    }
                }
            }
        }
    }
}