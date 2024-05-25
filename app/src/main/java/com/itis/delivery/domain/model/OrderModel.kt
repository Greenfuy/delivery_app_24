package com.itis.delivery.domain.model

class OrderModel(
    val id: String = "",
    val orderNumber: Long = 0L,
    val userId: String = "",
    val products: Map<String, String> = emptyMap(),
    val address: String = "",
    val lastChange: Long = 0,
    val state: Int = CREATED
) {
    companion object {
        const val CREATED = 0
        const val IN_PROGRESS = 1
        const val DELIVERED = 2
        const val CANCELLED = 3
    }
}