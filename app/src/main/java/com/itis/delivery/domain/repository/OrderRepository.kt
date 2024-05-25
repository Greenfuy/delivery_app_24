package com.itis.delivery.domain.repository

import com.itis.delivery.domain.model.OrderModel

interface OrderRepository {

    suspend fun getOrders() : List<OrderModel>

    suspend fun addOrder(products: Map<Long, Long>, address: String) : Boolean

    suspend fun getOrder(orderNumber: Long) : OrderModel

    suspend fun cancelOrder(orderId: String) : Boolean
}