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
class PaymentPurchaseRepository @Inject constructor(private val paymentPurchaseDao: PaymentPurchaseDao):  BaseRepository(){

    fun getBetweenDates(startDate: Long, finishDate: Long): LiveData<Resource<List<PaymentPurchase>>>{
        return object : Processor<List<PaymentPurchase>, LiveData<List<PaymentPurchase>>>(){
            override suspend fun query():  LiveData<List<PaymentPurchase>> = paymentPurchaseDao.getBetweenDates(startDate, finishDate)
            override fun validate(response: List<PaymentPurchase>): Int = if(response.isNotEmpty()){
                AppCode.SUCCESS_QUERY_DATABASE

            }else{
                AppCode.ERROR_QUERY_DATABASE
            }
        }.asResult()
    }
}