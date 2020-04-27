package com.titaniocorp.pos.app.viewmodel.model

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.titaniocorp.pos.app.model.Price
import com.titaniocorp.pos.util.formatMoney

class PriceViewModel(price: Price?): ViewModel(){
    private val price = checkNotNull(price)

    val name = ObservableField(this.price.name)
    val sku = ObservableField(this.price.sku)
    val cost = ObservableField(this.price.cost.formatMoney())
    val stock = ObservableField(this.price.stock)
}