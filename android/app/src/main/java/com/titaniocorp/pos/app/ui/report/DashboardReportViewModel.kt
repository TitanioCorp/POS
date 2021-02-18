package com.titaniocorp.pos.app.ui.report

import androidx.databinding.Bindable
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import com.titaniocorp.pos.BR
import com.titaniocorp.pos.app.viewmodel.ObservableViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class DashboardReportViewModel @Inject constructor(): ObservableViewModel() {
    val navigate = LiveEvent<Pair<Int, Pair<Date,Date>?>>()
    @Bindable var selectedReport = 0

    @Bindable val startDate: Calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    }

    @Bindable val endDate: Calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
    }

    fun selectReport(position: Int) {
        selectedReport = position
        notifyPropertyChanged(BR.selectedReport)
    }

    fun selectStartDate(year: Int, month: Int, dayOfMonth: Int){
        with(startDate){
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, dayOfMonth)

            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        notifyPropertyChanged(BR.startDate)
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
        notifyPropertyChanged(BR.endDate)
    }

    suspend fun getDates(): Pair<Long, Long> = withContext(Dispatchers.IO){
        Pair(startDate.timeInMillis, endDate.timeInMillis)
    }

    fun navigate(){
        navigate.value = Pair(selectedReport, Pair(startDate.time, endDate.time))
    }
}