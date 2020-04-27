package com.titaniocorp.pos.app.ui.billing

import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import com.titaniocorp.pos.BR
import com.titaniocorp.pos.app.model.Billing
import com.titaniocorp.pos.app.model.Resource
import com.titaniocorp.pos.app.viewmodel.ObservableViewModel
import com.titaniocorp.pos.repository.PaymentPurchaseRepository
import com.titaniocorp.pos.repository.PurchaseRepository
import com.titaniocorp.pos.repository.StockRepository
import com.titaniocorp.pos.repository.WarehouseRepository
import com.titaniocorp.pos.util.CSVUtil
import com.titaniocorp.pos.util.Constants
import com.titaniocorp.pos.util.MailUtil
import com.titaniocorp.pos.util.TypeEmail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class DashboardBillingViewModel @Inject constructor(
    purchaseRepository: PurchaseRepository,
    paymentPurchaseRepository: PaymentPurchaseRepository,
    stockRepository: StockRepository,
    warehouseRepository: WarehouseRepository
): ObservableViewModel() {
    private final val dates = MutableLiveData<Pair<Long, Long>>()

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

    @Bindable val billing = Billing()
    val purchases = Transformations.switchMap(dates){ dates ->
        purchaseRepository.getBetweenDates(dates.first, dates.second)
    }

    val paymentsPurchases = Transformations.switchMap(dates) { dates ->
        paymentPurchaseRepository.getBetweenDates(dates.first, dates.second)
    }

    val stocks = Transformations.switchMap(dates) { dates ->
        stockRepository.getBetweenDates(dates.first, dates.second)
    }

    val payments = Transformations.switchMap(dates) { dates ->
        warehouseRepository.getBetweenDates(dates.first, dates.second)
    }

    //region Dates
    fun selectStartDate(year: Int, month: Int, dayOfMonth: Int){
        with(startDate){
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, dayOfMonth)
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

    fun computePurchases(){
        purchases.value?.data?.forEach {purchase ->
            with(billing){
                cost += purchase.cost
                profit += purchase.profit
                adjustment += purchase.adjustment
                tax += purchase.tax

                totalPurchase += purchase.total
            }
        }
    }

    fun computePaymentsPurchases(){
        paymentsPurchases.value?.data?.forEach { payment ->
            with(billing){
                when{
                    payment.value < 0 -> {
                        refund += payment.value
                        totalExpenses += payment.value
                    }

                    else -> {
                        totalIncome += payment.value
                        if(payment.isCredit){
                            payments += payment.value
                        }
                    }
                }
            }
        }
    }

    fun computePayments(){
        payments.value?.data?.forEach { payment ->
            with(billing){
                when{
                    payment.value < 0 -> {
                        expenses += payment.value
                        totalExpenses += payment.value
                    }

                    else -> {
                        income += payment.value
                        totalIncome += payment.value
                    }
                }
            }
        }
    }

    private fun computeStock(){
        stocks.value?.data?.forEach {
            billing.stock += it.total
            billing.totalExpenses += it.total * -1
        }
    }

    fun computeBilling(){
        viewModelScope.launch(Dispatchers.Default){
            with(billing){
                cost = 0.0
                profit = 0.0
                refund = 0.0
                payments = 0.0
                adjustment = 0.0
                tax = 0.0
                stock = 0.0
                expenses = 0.0
                income = 0.0
                totalPurchase = 0.0
                totalExpenses = 0.0
                totalIncome = 0.0
                total = 0.0

                computePurchases()
                computePaymentsPurchases()
                computeStock()
                computePayments()

                total = totalIncome + totalExpenses
            }
            notifyPropertyChanged(BR.billing)
        }

    }

    fun sendMail(type: TypeEmail = TypeEmail.BILLING): LiveData<Resource<String>> {
        val result = LiveEvent<Resource<String>>()

        viewModelScope.launch(Dispatchers.Default){
            result.postValue(Resource.loading(null))

            try {
                val billingFile = CSVUtil.getBillingFile(billing)

                val purchasesFile = purchases.value?.data?.let {
                    if(it.isNotEmpty()){
                        CSVUtil.getPurchasesFile(it)
                    }else{
                        null
                    }}

                val paymentsPurchasesFile = paymentsPurchases.value?.data?.let {
                    if(it.isNotEmpty()){
                        CSVUtil.getPaymentsPurchaseFile(it)
                    }else{
                        null
                    }}

                val stocksFile = stocks.value?.data?.let {
                    if(it.isNotEmpty()){
                        CSVUtil.getStocksFile(it)
                    }else{
                        null
                    }}

                val paymentsFile = payments.value?.data?.let {
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