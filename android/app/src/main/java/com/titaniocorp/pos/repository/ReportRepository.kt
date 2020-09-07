package com.titaniocorp.pos.repository

import com.titaniocorp.pos.app.model.*
import com.titaniocorp.pos.app.model.domain.StockReportItem
import com.titaniocorp.pos.database.dao.*
import com.titaniocorp.pos.repository.processor.*
import com.titaniocorp.pos.util.AppCode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Maneja todos las ejecuciones con la base de datos de clientes(customer)
 * @author Juan Ortiz
 * @date 09/01/2020
 */
@ExperimentalCoroutinesApi
class ReportRepository @Inject constructor(
    private val reportDao: ReportDao
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
}