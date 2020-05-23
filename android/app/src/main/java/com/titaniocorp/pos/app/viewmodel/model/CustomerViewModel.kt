package com.titaniocorp.pos.app.viewmodel.model

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.titaniocorp.pos.app.model.CustomerEntity
import com.titaniocorp.pos.app.model.domain.Customer

class CustomerViewModel(item: Customer?): ViewModel(){
    private val item = checkNotNull(item)

    val name = ObservableField(this.item.name)
    val dni = ObservableField(this.item.dni.toString())
    val phone = ObservableField(this.item.phone.toString())
}