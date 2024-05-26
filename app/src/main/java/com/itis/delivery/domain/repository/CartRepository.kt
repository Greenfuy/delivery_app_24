package com.itis.delivery.domain.repository

import com.itis.delivery.domain.model.CartDomainModel

interface CartRepository {

    suspend fun getInCartCount(productId: Long): Long

    suspend fun addToCart(productId: Long) : Boolean

    suspend fun removeFromCart(productId: Long) : Boolean

    suspend fun isInCart(productId: Long) : Boolean

    suspend fun getCartList() : List<CartDomainModel>

    suspend fun getCartListByProductIndices(vararg productIds: Long) : List<CartDomainModel>

    suspend fun removeAll(vararg productIds: Long) : Boolean
}