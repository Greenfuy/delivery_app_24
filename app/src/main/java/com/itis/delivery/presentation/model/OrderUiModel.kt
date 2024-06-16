package com.itis.delivery.presentation.model

class OrderUiModel(
    val orderNumber: Long,
    val state: Int,
    val createdTime: Long,
    val lastChange: Long,
    val address: String,
    val products: List<OrderProductUiModel>
)

class OrderProductUiModel(
    val productId: Long,
    val name: String,
    val productImageUrl: String,
    val price: Long,
    val count: Long
)