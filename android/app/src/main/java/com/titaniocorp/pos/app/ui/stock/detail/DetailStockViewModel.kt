package com.titaniocorp.pos.app.ui.stock.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.titaniocorp.pos.app.model.Resource
import com.titaniocorp.pos.app.model.Stock
import com.titaniocorp.pos.app.viewmodel.ObservableViewModel
import com.titaniocorp.pos.repository.StockRepository
import javax.inject.Inject

class DetailStockViewModel @Inject constructor(
    private val stockRepository: StockRepository
): ObservableViewModel() {
    private val searchedId = MutableLiveData<Long>()
    val stock: LiveData<Resource<Stock>> = Transformations.switchMap(searchedId){ query ->
        stockRepository.getById(query)
    }

    fun setId(id: Long){
        searchedId.value = id
    }
}