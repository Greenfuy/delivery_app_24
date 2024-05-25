package com.itis.delivery.presentation.model

class CartProductModel(
    val productName: String,
    val productImageUrl: String,
    val price: Int,
    var isChosen: Boolean = true,
    val productId: Long,
    val count: Long
)