package com.itis.delivery.domain.repository

interface RateRepository {

    suspend fun getRateAvgByProductId(productId: Long) : Double

    suspend fun addRate(productId: Long, rate: Int) : Boolean

    suspend fun isProductRated(productId: Long) : Boolean
}