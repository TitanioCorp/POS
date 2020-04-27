package com.titaniocorp.pos.app.viewmodel.model

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.titaniocorp.pos.app.model.Payment

class PaymentViewModel(data: Payment?): ViewModel(){
    private val item = checkNotNull(data)

    val id = ObservableField(item.id)
    val paymentCategoryId = ObservableField(item.paymentCategoryId)
    val value = ObservableField(item.value)
    val date = ObservableField(item.date)
}