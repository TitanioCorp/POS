package com.titaniocorp.pos.app.viewmodel.model

import androidx.lifecycle.ViewModel
import com.titaniocorp.pos.app.model.PricePurchase
import com.titaniocorp.pos.app.model.Profit

class PricePurchaseViewModel(item: Pair<PricePurchase, Profit>?): ViewModel(){
    private val price = checkNotNull(item?.first)
    private val profit = checkNotNull(item?.second)
}