package com.itis.delivery.domain.repository

import com.itis.delivery.domain.model.OrderDomainModel

interface OrderRepository {

    suspend fun getOrders() : List<OrderDomainModel>

    suspend fun addOrder(
        products: LongArray,
        counts: LongArray,
        prices: LongArray,
        address: String
    ) : Boolean

    suspend fun getOrder(orderNumber: Long) : OrderDomainModel

    suspend fun cancelOrder(orderNumber: Long) : Boolean
}