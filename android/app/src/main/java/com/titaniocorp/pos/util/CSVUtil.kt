package com.titaniocorp.pos.util

import com.titaniocorp.pos.app.model.*
import java.io.File
import java.util.*

object CSVUtil {
    private const val CSV_HEADER_PURCHASES = "id,\"Cliente Id\",Costo,ganancia,Ajustes,Reembolsos,Impuestos,Subtotal,Total,\"Es credito\""
    private const val CSV_HEADER_PAYMENTS_PURCHASE = "id,\"Purchase Id\",\"Payment Category Id\",Valor,\"Es crédito\",Fecha"
    private const val CSV_HEADER_STOCKS = "id,\"Purchase Reference\",Costo,Impuestos,Total"
    private const val CSV_HEADER_PAYMENTS = "id,\"Payments Category Id\",Valor,Observacion"

    fun getBillingFile(billing: Billing): File?{
        Configurations.directory?.let {
            return FileUtil.createFile(it, "billing", "csv").apply {
                printWriter().use { out ->
                    with(billing){
                        out.println("Costos,${cost}")
                        out.println("Ganacias,${profit}")
                        out.println("Reembolsos,${refund}")
                        out.println("Abonos,${payments}")
                        out.println("Ajustes,${adjustment}")
                        out.println("Impuestos,${tax}")
                        out.println("Compra de inventario,${stock}")
                        out.println("Otros egresos,${expenses}")
                        out.println("Otros ingresos,${income}")
                        out.println("Total ventas,${totalPurchase}")
                        out.println("Total rgresos,${totalExpenses}")
                        out.println("Total ingresos,${totalIncome}")
                        out.println("Total,${total}")
                        out.println("Fecha,${Date().toFormatString()}")
                    }
                }
            }
        }

        return null
    }

    fun getPurchasesFile(list: List<Purchase>): File?{
        Configurations.directory?.let {
            return FileUtil.createFile(it, "purchases", "csv").apply {
                printWriter().use { out ->
                    out.println(CSV_HEADER_PURCHASES)
                    list.forEach {purchase ->
                        out.println("${purchase.id},${purchase.customerId},${purchase.cost},${purchase.profit},${purchase.adjustment},${purchase.refund},${purchase.tax},${purchase.subtotal},${purchase.total},${purchase.isCredit}")
                    }
                }
            }
        }

        return null
    }

    fun getPaymentsPurchaseFile(list: List<PaymentPurchase>): File?{
        Configurations.directory?.let {
            return FileUtil.createFile(it, "payments-purchase", "csv").apply {
                printWriter().use { out ->
                    out.println(CSV_HEADER_PAYMENTS_PURCHASE)
                    list.forEach {item ->
                        out.println("${item.id},${item.purchaseId},${item.paymentCategoryId},${item.value},${item.isCredit},${item.createdDate.toFormatString()}")
                    }
                }
            }
        }

        return null
    }

    fun getStocksFile(list: List<Stock>): File?{
        Configurations.directory?.let {
            return FileUtil.createFile(it, "stocks", "csv").apply {
                printWriter().use { out ->
                    out.println(CSV_HEADER_STOCKS)
                    list.forEach {item ->
                        out.println("${item.id},${item.purchaseRef},${item.cost},${item.tax},${item.total}")
                    }
                }
            }
        }

        return null
    }

    fun getPaymentsFile(list: List<Payment>): File?{
        Configurations.directory?.let {
            return FileUtil.createFile(it, "payments", "csv").apply {
                printWriter().use { out ->
                    out.println(CSV_HEADER_PAYMENTS)
                    list.forEach {item ->
                        out.println("${item.id},${item.paymentCategoryId},${item.value},${item.observation}")
                    }
                }
            }
        }

        return null
    }
}