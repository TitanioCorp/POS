package com.titaniocorp.pos.app.model

import java.util.*

data class Billing(
    var id: Long = 0,
    var cost: Double = 0.0,
    var profit: Double = 0.0,
    var refund: Double = 0.0,
    var payments: Double = 0.0,
    var adjustment: Double = 0.0,
    var tax: Double = 0.0,
    var stock: Double = 0.0,
    var expenses: Double = 0.0,
    var income: Double = 0.0,
    var totalPurchase: Double = 0.0,
    var totalExpenses: Double = 0.0,
    var totalIncome: Double = 0.0,
    var total: Double = 0.0,
    var createdDate: Date = Date()
){
    var purchases: List<Purchase> = listOf()
    var paymentPurchases: List<PaymentPurchase> = listOf()
    var paymentsList: List<Payment> = listOf()
    var stocks: List<Stock> = listOf()

    private fun reset(){
        id = 0
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
    }

    fun compute(){
        reset()

        purchases.forEach {purchase ->
            cost += purchase.cost
            profit += purchase.profit
            adjustment += purchase.adjustment
            tax += purchase.tax

            totalPurchase += purchase.total
        }

        paymentPurchases.forEach { payment ->
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

        paymentsList.forEach { payment ->
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

        stocks.forEach {
            stock += it.total
            totalExpenses += it.total * -1
        }

        total = totalIncome + totalExpenses
    }
}