package com.titaniocorp.pos.app.ui.purchase.detail

import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.titaniocorp.pos.BR
import com.titaniocorp.pos.app.model.PaymentPurchase
import com.titaniocorp.pos.app.model.Purchase
import com.titaniocorp.pos.app.model.Resource
import com.titaniocorp.pos.app.model.dto.DetailPurchaseAdapterDto
import com.titaniocorp.pos.app.viewmodel.ObservableViewModel
import com.titaniocorp.pos.repository.PurchaseRepository
import com.titaniocorp.pos.util.toLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class DetailPurchaseViewModel @Inject constructor(
    private val purchaseRepository: PurchaseRepository
): ObservableViewModel() {
    private val mPurchaseId = MutableLiveData<Long>()
    @Bindable var totalPayments: Double = 0.0
    @Bindable var totalMissingPayment: Double = 0.0

    val purchase: LiveData<Resource<Purchase>> = Transformations.switchMap(mPurchaseId){id ->
        purchaseRepository.getByIdAsLiveData(id)
    }.toLiveEvent()

    val pricesDto: LiveData<Resource<List<DetailPurchaseAdapterDto>>> = Transformations.switchMap(mPurchaseId){ id ->
        purchaseRepository.getPriceDtoById(id)
    }.toLiveEvent()

    fun setPurchaseId(id: Long){
        mPurchaseId.value = id
    }

    fun canRefund(): Boolean = purchase.value?.data?.prices?.any{ !it.isRefund } ?: false

    fun addRefund(pricePurchaseId: Long, quantity: Int): LiveData<Resource<Pair<Long, Int>>>?{
        purchase.value?.data?.let{
            val purchase = it.copy()
            purchase.prices.find {item -> item.id == pricePurchaseId }?.let {pricePurchase ->
                val refund = pricePurchase.copy(id = 0, quantity = quantity, createdDate = Date(), isRefund = true)
                pricePurchase.quantity -= quantity

                val valueRefund = ((refund.cost + refund.profit + refund.tax) * quantity) * -1
                val paymentPurchase = PaymentPurchase(0, purchase.id, 1, valueRefund, false)

                purchase.prices.add(refund)

                computePurchaseRefund(purchase)

                /*Timber.tag(TAG_APP_DEBUG).d(purchase.toJson())
                Timber.tag(TAG_APP_DEBUG).d(paymentPurchase.toJson())*/

                return purchaseRepository.addRefund(purchase, paymentPurchase, pricePurchase, refund)
            }
        }
        return null
    }

    fun getMissingPayment(): Double = (purchase.value?.data?.total ?: 0.0) - totalPayments

    fun canAddPayment(): Boolean = ((purchase.value?.data?.total ?: 0.0) - totalPayments > 0 && purchase.value?.data?.isCredit ?: false)

    fun addPayment(value: Double): LiveData<Resource<List<PaymentPurchase>>>?{
        purchase.value?.data?.let{
            return purchaseRepository.addPayment(PaymentPurchase(0, it.id, 1, value, true))
        }
        return null
    }

    suspend fun computePricesDto(data: List<DetailPurchaseAdapterDto>?): Pair<List<DetailPurchaseAdapterDto>?, List<DetailPurchaseAdapterDto>?> = withContext(Dispatchers.Default){
        val prices = data?.filter { item -> !item.refund}
        val refunds = data?.filter {item -> item.refund}

        /*refunds?.forEach {refund ->
            prices?.find{item -> item.priceId == refund.priceId }?.let {
                it.quantity += refund.quantity
            }
        }*/

        Pair(prices, refunds)
    }

    fun setPayments(list: List<PaymentPurchase>?){
        list?.let {
            purchase.value?.data?.payments?.clear()
            purchase.value?.data?.payments?.addAll(list)
        }
    }

    suspend fun computePurchase(): List<PaymentPurchase>? = withContext(Dispatchers.Default){
        totalPayments = 0.0

        purchase.value?.data?.let{
            if(it.isCredit){
                it.payments
                    .filter {item -> item.isCredit && item.value > 0}
                    .also {list ->
                        list.forEach{payment -> totalPayments += payment.value }

                        totalMissingPayment = it.total - totalPayments

                        if(totalMissingPayment < 0){
                            totalMissingPayment = 0.0
                        }
                        notifyPropertyChanged(BR.totalPayments)
                        notifyPropertyChanged(BR.totalMissingPayment)
                    }
            }else{
                null
            }
        }
    }

    private fun computePurchaseRefund(purchase: Purchase){
        with(purchase){
            cost = 0.0
            profit = 0.0
            refund = 0.0
            tax = 0.0

            prices.forEach {
                if(!it.isRefund){
                    cost += it.cost * it.quantity
                    profit += it.profit * it.quantity
                    tax += it.tax * it.quantity
                }else{
                    refund += ((it.cost + it.profit  + tax) * it.quantity) * -1
                }
            }

            subtotal = cost + profit
            total = cost + profit + adjustment + tax

            if(total < 0){
                total = 0.0
            }
        }
    }
}