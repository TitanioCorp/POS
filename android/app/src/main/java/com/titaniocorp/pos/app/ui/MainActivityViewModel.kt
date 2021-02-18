package com.titaniocorp.pos.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.titaniocorp.pos.app.model.InitialProfit
import com.titaniocorp.pos.app.viewmodel.BaseViewModel
import com.titaniocorp.pos.repository.InitialProfitRepository
import com.titaniocorp.pos.repository.processor.asLiveEvent
import com.titaniocorp.pos.util.Configurations
import com.titaniocorp.pos.util.process
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MainActivityViewModel @Inject constructor(
    private val repository: InitialProfitRepository
): ViewModel() {

    fun setInitialProfitsConfigurations(){
        viewModelScope.launch(Dispatchers.IO){
            repository.getAll().collect {
                it.process({
                    it.data?.let {list ->
                        Configurations.initialProfits = list
                    }
                })
            }
        }
    }
}