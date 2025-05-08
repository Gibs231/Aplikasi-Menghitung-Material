package com.gibraltar0123.materialapp.model

data class CheckoutItem(
    val id: Long,
    val name: String,
    val pricePerPackage: Double,
    val quantity: Int,
    val imageResId: Int
)