package com.titaniocorp.pos.app.ui.profit.initial

import androidx.lifecycle.asLiveData
import com.titaniocorp.pos.app.model.InitialProfit
import com.titaniocorp.pos.app.viewmodel.BaseViewModel
import com.titaniocorp.pos.repository.InitialProfitRepository
import com.titaniocorp.pos.repository.processor.asLiveEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class DashboardInitialProfitViewModel @Inject constructor(
    private val repository: InitialProfitRepository
): BaseViewModel() {
    val initialProfits = repository.getAll().asLiveData()

    fun insert(item: InitialProfit) = repository.insert(item).asLiveEvent()
    fun update(item: InitialProfit) = repository.update(item)
    fun delete(item: InitialProfit) = repository.delete(item)
}