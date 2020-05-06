package com.titaniocorp.pos.repository

import androidx.lifecycle.*
import com.titaniocorp.pos.app.model.*
import com.titaniocorp.pos.app.model.dto.DetailPurchaseAdapterDto
import com.titaniocorp.pos.app.model.dto.PurchaseDTO
import com.titaniocorp.pos.database.dao.*
import com.titaniocorp.pos.repository.processor.*
import com.titaniocorp.pos.util.AppCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

/**
 * Maneja todos las ejecuciones con la base de datos de clientes(customer)
 * @author Juan Ortiz
 * @date 09/01/2020
 */
class PurchaseRepository @Inject constructor(
    private val purchaseDao: PurchaseDao,
    private val priceDao: PriceDao,
    private val pricePurchaseDao: PricePurchaseDao,
    private val paymentPurchaseDao: PaymentPurchaseDao
):  BaseRepository(){
    fun getById(id: Long): LiveData<Resource<Purchase>>{
        return object : Processor<Purchase, Purchase>(){
            override suspend fun query(): Purchase {
                return purchaseDao.getById(id).asDomainModel().also {
                    it.payments.addAll(paymentPurchaseDao.getSimpleAll(it.id))
                    it.prices.addAll(pricePurchaseDao.getSimpleAll(it.id))
                }
            }
            override fun validate(response: Purchase): Int = if(response.id > 0){
                AppCode.SUCCESS_QUERY_DATABASE
            }else{
                AppCode.ERROR_QUERY_DATABASE
            }
        }.asResult()
    }

    fun getByIdAsLiveData(id: Long): LiveData<Resource<Purchase>>{
        return object : Processor<Purchase, LiveData<Purchase>>(){
            override suspend fun query():  LiveData<Purchase> = Transformations.map(purchaseDao.getByIdAsLiveData(id)){
                it.asDomainModel()
            }
            override fun validate(response: Purchase): Int = if(response.id > 0){
                runBlocking(Dispatchers.IO){
                    response.payments.addAll(paymentPurchaseDao.getSimpleAll(id))
                    response.prices.addAll(pricePurchaseDao.getSimpleAll(id))
                }
                AppCode.SUCCESS_QUERY_DATABASE
            }else{
                AppCode.ERROR_QUERY_DATABASE
            }
        }.asResult()
    }

    fun getShortAll(): LiveData<Resource<List<PurchaseDTO>>>{
        return object : Processor<List<PurchaseDTO>, LiveData<List<PurchaseDTO>>>(){
            override suspend fun query():  LiveData<List<PurchaseDTO>> = purchaseDao.getLightAll()
            override fun validate(response: List<PurchaseDTO>): Int = if(response.isNotEmpty()){
                AppCode.SUCCESS_QUERY_DATABASE
            }else{
                AppCode.ERROR_QUERY_DATABASE
            }
        }.asResult()
    }

    fun getBetweenDates(startDate: Long, finishDate: Long): LiveData<Resource<List<Purchase>>>{
        return object : Processor<List<Purchase>, LiveData<List<Purchase>>>(){
            override suspend fun query():  LiveData<List<Purchase>> =  Transformations.map(purchaseDao.getBetweenDates(startDate, finishDate)){ it.asDomainModel()}
            override fun validate(response: List<Purchase>): Int = if(response.isNotEmpty()){
                runBlocking(Dispatchers.IO){
                    response.forEach {
                        it.payments.addAll(paymentPurchaseDao.getSimpleAll(it.id))
                        it.prices.addAll(pricePurchaseDao.getSimpleAll(it.id))
                    }
                }

                AppCode.SUCCESS_QUERY_DATABASE

            }else{
                AppCode.ERROR_QUERY_DATABASE
            }
        }.asResult()
    }

    fun getLightBetweenDates(startDate: Long, finishDate: Long): LiveData<Resource<List<PurchaseDTO>>>{
        return object : Processor<List<PurchaseDTO>, LiveData<List<PurchaseDTO>>>(){
            override suspend fun query():  LiveData<List<PurchaseDTO>> = purchaseDao.getLightBetweenDates(startDate, finishDate)
            override fun validate(response: List<PurchaseDTO>): Int = if(response.isNotEmpty()){
                AppCode.SUCCESS_QUERY_DATABASE
            }else{
                AppCode.ERROR_QUERY_DATABASE
            }
        }.asResult()
    }

    fun getPriceDtoById(id: Long): LiveData<Resource<List<DetailPurchaseAdapterDto>>>{
        return object : Processor<List<DetailPurchaseAdapterDto>, LiveData<List<DetailPurchaseAdapterDto>>>(){
            override suspend fun query():  LiveData<List<DetailPurchaseAdapterDto>> = purchaseDao.getPriceDtoById(id)
            override fun validate(response: List<DetailPurchaseAdapterDto>): Int = if(response.isNotEmpty()){
                AppCode.SUCCESS_QUERY_DATABASE
            }else{
                AppCode.ERROR_QUERY_DATABASE
            }
        }.asResult()
    }

    fun add(item: Purchase): LiveData<Resource<Pair<Long, Int>>>{
        return object : Processor<Pair<Long, Int>, Pair<Long, Int>>(true){
            override suspend fun query(): Pair<Long, Int> {
                val purchaseId = purchaseDao.insert(item.asDatabaseModel())

                if(purchaseId <= 0){ return Pair(0, 1) }

                item.payments.forEach { it.purchaseId = purchaseId }
                val paymentsInserted =  paymentPurchaseDao.insertAll(*item.payments.toTypedArray())

                if(paymentsInserted.isEmpty() && !item.isCredit){ return Pair(0, 1) }

                item.prices.forEach { it.purchaseId = purchaseId }
                val pricesInserted =  pricePurchaseDao.insertAll(*item.prices.toTypedArray())

                if(pricesInserted.isEmpty()){
                    return Pair(0, 1)
                }else{
                    item.prices.forEach {
                        priceDao.removeStock(it.priceId, it.quantity)
                    }
                }

                return Pair(purchaseId, 0)
            }
            override fun validate(response: Pair<Long, Int>): Int = response.second
        }.asResult()
    }

    fun addRefund(
        purchase: Purchase,
        paymentPurchase: PaymentPurchase,
        price: PricePurchase,
        refund: PricePurchase
    ): LiveData<Resource<Pair<Long, Int>>>{
        return object : Processor<Pair<Long, Int>, Pair<Long, Int>>(true){
            override suspend fun query(): Pair<Long, Int> {
                val purchaseId = purchaseDao.update(purchase.asDatabaseModel())
                if(purchaseId <= 0){ return Pair(0, 1) }

                val paymentInserted =  paymentPurchaseDao.insert(paymentPurchase)
                if(paymentInserted <= 0){ return Pair(0, 1) }

                val priceUpdated = if(price.quantity == 0){
                    pricePurchaseDao.delete(price)
                }else{
                    pricePurchaseDao.update(price)
                }

                if(priceUpdated <= 0){ return Pair(0, 1) }

                val priceInserted =  pricePurchaseDao.insert(refund)
                if(paymentInserted <= 0){ return Pair(0, 1) }

                return Pair(priceInserted, 0)
            }
            override fun validate(response: Pair<Long, Int>): Int = response.second
        }.asResult()
    }

    fun addPayment(payment: PaymentPurchase): LiveData<Resource<List<PaymentPurchase>>>{
        return object : Processor<List<PaymentPurchase>, List<PaymentPurchase>>(true){
            override suspend fun query(): List<PaymentPurchase> {
                return if(paymentPurchaseDao.insert(payment) > 0){
                    paymentPurchaseDao.getSimpleAll(payment.purchaseId)
                }else{
                    listOf()
                }
            }
            override fun validate(response:  List<PaymentPurchase>): Int = if(response.isNotEmpty()){
                AppCode.SUCCESS_QUERY_DATABASE
            }else{
                AppCode.ERROR_QUERY_DATABASE
            }
        }.asResult()
    }
}