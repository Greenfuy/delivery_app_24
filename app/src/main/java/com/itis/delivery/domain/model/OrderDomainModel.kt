package com.itis.delivery.domain.model

class OrderDomainModel(
    val orderData: OrderData = OrderData(),
    var orderProducts: List<OrderProduct> = emptyList()
) {

    fun addOrders(
        products: LongArray,
        counts: LongArray,
        prices: LongArray,
    ) {
        if (products.size != counts.size && counts.size != prices.size) return

        val orderProductsList = mutableListOf<OrderProduct>()

        products.forEachIndexed { id, productId ->
            orderProductsList.add(
                OrderProduct(
                    orderId = orderData.id,
                    productId = productId,
                    count = counts[id],
                    price = prices[id]
                )
            )
        }

        orderProducts = orderProductsList
    }

    companion object {
        const val CREATED = 0
        const val IN_PROGRESS = 1
        const val DELIVERED = 2
        const val CANCELLED = 3
    }
}

class OrderProduct(
    val orderId: String = "",
    val productId: Long = 0,
    val count: Long = 0,
    val price: Long = 0
)

class OrderData(
    val id: String = "",
    val orderNumber: Long = 0L,
    val userId: String = "",
    val address: String = "",
    val createdTime: Long = 0,
    val lastChange: Long = 0,
    val state: Int = OrderDomainModel.CREATED
)