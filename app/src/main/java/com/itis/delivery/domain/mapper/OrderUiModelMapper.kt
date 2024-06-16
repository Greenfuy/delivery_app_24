package com.itis.delivery.domain.mapper

import com.itis.delivery.domain.model.OrderDomainModel
import com.itis.delivery.domain.model.ProductDomainModel
import com.itis.delivery.presentation.model.OrderProductUiModel
import com.itis.delivery.presentation.model.OrderUiModel
import javax.inject.Inject

class OrderUiModelMapper @Inject constructor() {

    fun mapToOrderUiModelList(
        orderList: List<OrderDomainModel>,
        productList: List<ProductDomainModel>
        ): List<OrderUiModel> {

        return orderList.map { order ->
            mapToOrderUiModel(order, productList)
        }
    }

    fun mapToOrderUiModel(order: OrderDomainModel, productList: List<ProductDomainModel>) =
        OrderUiModel(
            orderNumber = order.orderData.orderNumber,
            state = order.orderData.state,
            createdTime = order.orderData.createdTime,
            lastChange = order.orderData.lastChange,
            address = order.orderData.address,
            products = order.orderProducts.mapNotNull { orderProduct ->
                productList.find { it.id == orderProduct.productId }?.let { product ->
                    OrderProductUiModel(
                        productId = product.id,
                        name = product.name,
                        productImageUrl = product.imageUrl,
                        price = orderProduct.price,
                        count = orderProduct.count
                    )
                }
            }
        )
}