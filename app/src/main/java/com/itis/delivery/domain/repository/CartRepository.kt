package com.itis.delivery.domain.repository

import com.itis.delivery.domain.model.CartModel

interface CartRepository {

    suspend fun getInCartCount(productId: Long): Long

    suspend fun addToCart(productId: Long) : Boolean

    suspend fun removeFromCart(productId: Long) : Boolean

    suspend fun isInCart(productId: Long) : Boolean

    suspend fun getCartList() : List<CartModel>

    suspend fun getCartListByProductIndices(vararg productIds: Long) : List<CartModel>

    suspend fun removeAll(vararg productIds: Long) : Boolean
}