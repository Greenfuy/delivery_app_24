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
    override suspend fun getProducts(page: Int): List<ProductDomainModel> {
        val response = api.getProducts(page)
        if (response == null || response.products.isEmpty()) {
            throw ResponseEmptyException("Products list is empty")
        } else {
            return mapper.mapResponseToDomainModelList(input = response)
        }
    }

    override suspend fun getProductById(productId: Long): ProductDomainModel {
        val response = api.getProductById(productId.toString())
        if (response == null || response.products.isEmpty()) {
            throw ResponseNotFullException("Products list is not full")
        } else {
            return mapper.mapResponseToDomainModelList(input = response).first()
        }
    }

    override suspend fun getProductsByIndices(vararg productIds: Long): List<ProductDomainModel> {
        val response = api.getProductsByIndices(productIds.joinToString(","))
        if (response == null || response.products.isEmpty()) {
            throw ResponseEmptyException("Products list is empty")
        } else {
            return mapper.mapResponseToDomainModelList(input = response)
        }
    }

    override suspend fun getProductsByIndicesAndSearchTerm(
        vararg productIds: Long,
        searchTerm: String,
        page: Int
    ): List<ProductDomainModel> {
        val response = api.getProductsByIndicesAndSearchTerm(
            productIds.joinToString(","),
            searchTerm,
            page
        )
        if (response == null || response.products.isEmpty()) {
            throw ResponseEmptyException("Products list is empty")
        } else {
            return mapper.mapResponseToDomainModelList(input = response)
        }
    }

    override suspend fun getProductsByCategory(
        categoryTag: String,
        page: Int
        ): List<ProductDomainModel> {
        val response = api.getProductsByCategory(categoryTag, page)
        if (response == null || response.products.isEmpty()) {
            throw ResponseEmptyException("Products list is empty")
        } else {
            return mapper.mapResponseToDomainModelList(input = response)
        }
    }

    override suspend fun getProductsBySearchTerm(
        searchTerm: String,
        page: Int
        ): List<ProductDomainModel> {
        val response = api.getProductsBySearchTerm(searchTerm, page)
        if (response == null || response.products.isEmpty()) {
            throw ResponseEmptyException("Products list is empty")
        } else {
            return mapper.mapResponseToDomainModelList(input = response)
        }
    }
}
