package com.titaniocorp.pos.app.ui.billing

import androidx.databinding.Bindable
import androidx.lifecycle.*
import com.hadilq.liveevent.LiveEvent
import com.titaniocorp.pos.BR
import com.titaniocorp.pos.app.model.Billing
import com.titaniocorp.pos.app.model.Resource
import com.titaniocorp.pos.app.viewmodel.ObservableViewModel
import com.titaniocorp.pos.repository.*
import com.titaniocorp.pos.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class DashboardBillingViewModel @Inject constructor(
    private val reportRepository: ReportRepository
): ObservableViewModel() {
    @Bindable var billing = Billing()
    private final val dates = MutableLiveData<Pair<Long, Long>>()

    fun updateBilling(billing: Billing){
        Logger.d("[Report]: ${billing.toJson()}")
        this.billing = billing
        notifyPropertyChanged(BR.billing)
    }

    private val startDate = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    }

    private val endDate = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_MONTH, get(Calendar.DAY_OF_MONTH) + 1)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    }

    //region Dates
    fun selectStartDate(year: Int, month: Int, dayOfMonth: Int){
        with(startDate){
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, dayOfMonth)

            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
    }

    fun selectEndDate(year: Int, month: Int, dayOfMonth: Int){
        with(endDate){
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, dayOfMonth)

            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }
    }

    fun updateDates(){
        dates.postValue(Pair(startDate.timeInMillis, endDate.timeInMillis))
    }
    //endregion

    fun generateReport(): LiveData<Resource<Billing>> = Transformations.switchMap(dates){ dates ->
        reportRepository
            .generateSalesReport(dates.first, dates.second)
            .asLiveData()
    }
}