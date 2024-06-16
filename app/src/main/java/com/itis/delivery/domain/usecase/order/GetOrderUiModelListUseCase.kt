package com.itis.delivery.domain.usecase.order

import com.itis.delivery.domain.mapper.OrderUiModelMapper
import com.itis.delivery.domain.repository.OrderRepository
import com.itis.delivery.domain.repository.ProductRepository
import com.itis.delivery.presentation.model.OrderUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetOrderUiModelListUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val orderRepository: OrderRepository,
    private val dispatcher: CoroutineDispatcher,
    private val mapper: OrderUiModelMapper
) {

    suspend operator fun invoke(count: Int = 3) : List<OrderUiModel> {
        return withContext(dispatcher) {
            val orders = orderRepository.getOrders()

            val products = productRepository.getProductsByIndices(productIds =
                orders
                    .map { it.orderProducts.map { p -> p.productId }.take(count) }
                    .flatten()
                    .toLongArray()
            )

            mapper.mapToOrderUiModelList(orderList = orders, productList = products)
        }
    }
}