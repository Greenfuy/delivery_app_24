package com.itis.delivery.data.repository

import com.itis.delivery.data.exceptions.ResponseEmptyException
import com.itis.delivery.data.exceptions.ResponseNotFullException
import com.itis.delivery.data.mapper.ProductDomainModelMapper
import com.itis.delivery.data.remote.OpenFoodFactsApi
import com.itis.delivery.domain.model.ProductDomainModel
import com.itis.delivery.domain.repository.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: OpenFoodFactsApi,
    private val mapper: ProductDomainModelMapper
) : ProductRepository {
    override suspend fun getProducts(): List<ProductDomainModel> {
        val response = api.getProducts()
        if (response == null || response.products.isEmpty()) {
            throw ResponseEmptyException("Products list is empty")
        } else {
            return mapper.mapResponseToDomainModelList(input = response)
        }
    }

    override suspend fun getProductById(productId: Long): ProductDomainModel {
        val response = api.getProductById(productId)
        if (response == null || response.products.isEmpty()) {
            throw ResponseEmptyException("Product($productId) is empty")
        } else {
            val products = mapper.mapResponseToDomainModelList(input = response)
            if (products.isEmpty()) {
                throw ResponseNotFullException("Product($productId) is not full")
            } else {
                return products.first()
            }
        }
    }

    override suspend fun getProductsByCategory(categoryTag: String): List<ProductDomainModel> {
        val response = api.getProductsByCategory(categoryTag)
        if (response == null || response.products.isEmpty()) {
            throw ResponseEmptyException("Products list is empty")
        } else {
            return mapper.mapResponseToDomainModelList(input = response)
        }
    }

    override suspend fun getProductsBySearchTerm(searchTerm: String): List<ProductDomainModel> {
        val response = api.getProductsBySearchTerm(searchTerm)
        if (response == null || response.products.isEmpty()) {
            throw ResponseEmptyException("Products list is empty")
        } else {
            return mapper.mapResponseToDomainModelList(input = response)
        }
    }
}
