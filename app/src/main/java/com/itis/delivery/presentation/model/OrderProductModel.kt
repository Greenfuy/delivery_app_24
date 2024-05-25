package com.itis.delivery.presentation.model

class OrderProductModel(
    val orderNumber: Long = 0L,
    val productId: Long = 0L,
    val count: Long = 0L,
    val productName: String = "",
    val productImageUrl: String = "",
    val price: Int = 0,
    val address: String = ""
)