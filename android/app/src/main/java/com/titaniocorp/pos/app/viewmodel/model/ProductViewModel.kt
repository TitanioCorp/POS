package com.titaniocorp.pos.app.viewmodel.model

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.titaniocorp.pos.app.model.Product

class ProductViewModel(item: Product?): ViewModel(){
    private val item = checkNotNull(item)

    val name = ObservableField(this.item.name)
}