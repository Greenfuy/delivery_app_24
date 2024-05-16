package com.itis.delivery.domain.model

data class CartModel(
    val id: String,
    val productId: Long,
    val userId: String,
    val count: Long
)