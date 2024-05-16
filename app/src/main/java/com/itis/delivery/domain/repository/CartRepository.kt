package com.itis.delivery.domain.repository

interface CartRepository {

    suspend fun getInCartCount(userId: String, productId: Long): Long

    suspend fun addToCart(userId: String, productId: Long) : Boolean

    suspend fun removeFromCart(userId: String, productId: Long) : Boolean

    suspend fun isInCart(userId: String, productId: Long) : Boolean
}