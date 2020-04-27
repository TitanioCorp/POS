package com.titaniocorp.pos.app.viewmodel.model

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.titaniocorp.pos.app.model.Profit

class ProfitViewModel(item: Profit?): ViewModel(){
    private val item = checkNotNull(item)

    val name = ObservableField(this.item.name)
    val percent = ObservableField(this.item.percent.toString())
}