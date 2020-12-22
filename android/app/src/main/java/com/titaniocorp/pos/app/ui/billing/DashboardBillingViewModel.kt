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

    fun sendMail(type: TypeEmail = TypeEmail.BILLING): LiveData<Resource<String>> {
        val result = LiveEvent<Resource<String>>()

        viewModelScope.launch(Dispatchers.Default){
            result.postValue(Resource.loading(null))

            try {
                val billingFile = CSVUtil.getBillingFile(billing)

                val purchasesFile = billing.purchases.let {
                    if(it.isNotEmpty()){
                        CSVUtil.getPurchasesFile(it)
                    }else{
                        null
                    }}

                val paymentsPurchasesFile = billing.paymentPurchases.let {
                    if(it.isNotEmpty()){
                        CSVUtil.getPaymentsPurchaseFile(it)
                    }else{
                        null
                    }}

                val stocksFile = billing.stocks.let {
                    if(it.isNotEmpty()){
                        CSVUtil.getStocksFile(it)
                    }else{
                        null
                    }}

                val paymentsFile = billing.paymentsList.let {
                    if(it.isNotEmpty()){
                        CSVUtil.getPaymentsFile(it)
                    }else{
                        null
                    }}

                when(type){
                    TypeEmail.BILLING -> {
                        val isSuccess = MailUtil.sendBilling(
                            billing,
                            billingFile,
                            purchasesFile,
                            paymentsPurchasesFile,
                            stocksFile,
                            paymentsFile
                        )

                        isSuccess?.let {
                            result.postValue(Resource.error(isSuccess, 1))
                        } ?: run {
                            result.postValue(Resource.success(isSuccess))
                        }
                    }

                    TypeEmail.REPORT -> {
                        MailUtil.sendReport(
                            startDate.time,
                            endDate.time,
                            billing,
                            billingFile,
                            purchasesFile,
                            paymentsPurchasesFile,
                            stocksFile,
                            paymentsFile
                        )

                        result.postValue(Resource.success("Success"))
                    }
                }
            }catch (exception: Exception){
                Timber.tag(Constants.TAG_APP_DEBUG).e(exception)
                result.postValue(Resource.error(null, -1))
            }

        }


        return result
    }
}