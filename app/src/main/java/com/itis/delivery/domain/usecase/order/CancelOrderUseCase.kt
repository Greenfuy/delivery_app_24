package com.itis.delivery.domain.usecase.order

import com.itis.delivery.domain.repository.OrderRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CancelOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository,
    private val dispatcher: CoroutineDispatcher,
) {

    suspend operator fun invoke(orderNumber: Long) : Boolean {
        return withContext(dispatcher) {
            orderRepository.cancelOrder(orderNumber)
        }
    }
}