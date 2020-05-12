package com.titaniocorp.pos.app.ui.purchase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import com.titaniocorp.pos.app.model.Purchase
import com.titaniocorp.pos.app.model.Resource
import com.titaniocorp.pos.app.model.SearchPurchase
import com.titaniocorp.pos.app.viewmodel.BaseViewModel
import com.titaniocorp.pos.repository.PurchaseRepository
import java.util.*
import javax.inject.Inject

class DashboardPurchaseViewModel @Inject constructor(
    val purchaseRepository: PurchaseRepository
): BaseViewModel() {
    private val mAction = MutableLiveData<Pair<Long, Long>?>()

    val purchases = Transformations.switchMap(mAction){action ->
        action?.let {
            purchaseRepository.getLightBetweenDates(action.first, action.second)
        } ?: run {
            purchaseRepository.getShortAll()
        }
    }

    init {
        loadToday()
    }

    fun loadToday(){
        val startDate = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        val finishDate = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, get(Calendar.DAY_OF_MONTH) + 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        searchBetween(startDate.timeInMillis, finishDate.timeInMillis)
    }

    fun searchBetween(startDate: Long ?= null, finishDate: Long ?= null){
        mAction.value = if(startDate != null && finishDate != null){
            Pair(startDate, finishDate)
        }else{
            null
        }
    }


    // Flow
    private val mSearch = MutableLiveData<SearchPurchase>()

}