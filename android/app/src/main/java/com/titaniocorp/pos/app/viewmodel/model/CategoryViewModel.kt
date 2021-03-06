package com.titaniocorp.pos.app.viewmodel.model

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.titaniocorp.pos.app.model.Category
import com.titaniocorp.pos.app.model.Profit

class CategoryViewModel(data: Category?): ViewModel(){
    private val item = checkNotNull(data)
    val name = ObservableField(item.name)
}