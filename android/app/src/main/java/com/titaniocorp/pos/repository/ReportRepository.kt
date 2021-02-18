package com.titaniocorp.pos.repository

import com.titaniocorp.pos.app.model.*
import com.titaniocorp.pos.app.model.domain.StockReportItem
import com.titaniocorp.pos.database.dao.*
import com.titaniocorp.pos.database.entity.asDomainModel
import com.titaniocorp.pos.repository.processor.*
import com.titaniocorp.pos.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * Maneja todos las ejecuciones con la base de datos de clientes(customer)
 * @author Juan Ortiz
 * @date 09/01/2020
 */
@ExperimentalCoroutinesApi
class ReportRepository @Inject constructor(
    private val reportDao: ReportDao,

    private val purchaseDao: PurchaseDao,
    private val paymentPurchaseDao: PaymentPurchaseDao,
    private val stockDao: StockDao,
    private val paymentDao: PaymentDao
):  BaseRepository(){

    fun getPricesForStockReport(): Flow<Resource<List<StockReportItem>>> {
        return object: FlowProcessor<List<StockReportItem>, List<StockReportItem>>(){
            override fun query(): Flow<List<StockReportItem>> = reportDao.getPricesForStockReport()

            override fun validate(response: List<StockReportItem>): Int = if(response.isNotEmpty()){
                AppCode.SUCCESS_QUERY_DATABASE
            }else{
                AppCode.ERROR_QUERY_DATABASE
            }
        }.process()
    }

    fun generateSalesReport(startDate: Long, finalDate: Long): Flow<Resource<Billing>>{
        return flow<Resource<Billing>>{
            Billing().apply {
                purchases = purchaseDao.getBetweenDates(startDate, finalDate).asDomainModel()
                paymentPurchases = paymentPurchaseDao.getBetweenDates(startDate, finalDate).asDomainModel()
                paymentsList = paymentDao.getBetweenDates(startDate, finalDate).asDomainModel()
                stocks = stockDao.getBetweenDates(startDate, finalDate).asDomainModel()

                compute()
            }.also {
                emit(Resource.success(it))
            }
        }
            .onStart {
                emit(Resource.loading(null))
            }
            .catch {exception ->
                with(exception){
                    emit(Resource.error(null, getCode(), message))
                }
            }
            .flowOn(Dispatchers.IO)
    }
}