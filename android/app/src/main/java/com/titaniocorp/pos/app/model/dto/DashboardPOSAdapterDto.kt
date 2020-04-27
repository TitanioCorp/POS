package com.titaniocorp.pos.app.model.dto

data class DashboardPOSAdapterDto(
    val nameProduct: String,
    val namePrice: String,
    val nameProfit: String,
    val cost: Double,
    val tax: Double,
    val profit: Double,
    val quantity: Int,
    val total: Double
)