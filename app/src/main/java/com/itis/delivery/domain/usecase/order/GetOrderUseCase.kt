package com.itis.delivery.domain.usecase.order

import com.itis.delivery.domain.mapper.OrderUiModelMapper
import com.itis.delivery.domain.repository.OrderRepository
import com.itis.delivery.domain.repository.ProductRepository
import com.itis.delivery.presentation.model.OrderUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository,
    private val dispatcher: CoroutineDispatcher,
    private val mapper: OrderUiModelMapper
) {

    suspend operator fun invoke(orderNumber: Long) : OrderUiModel {
        return withContext(dispatcher) {
            val order = orderRepository.getOrder(orderNumber)

            val products = productRepository.getProductsByIndices(
                productIds = order.orderProducts.map { it.productId }.toLongArray()
            )

            mapper.mapToOrderUiModel(order, products)
        }
    }
}