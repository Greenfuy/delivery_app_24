package com.itis.delivery.domain.usecase.product

import com.itis.delivery.domain.mapper.OrderProductMapper
import com.itis.delivery.domain.repository.OrderRepository
import com.itis.delivery.domain.repository.ProductRepository
import javax.inject.Inject

class GetOrderProductListUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val orderRepository: OrderRepository,
    private val mapper: OrderProductMapper
) {
}