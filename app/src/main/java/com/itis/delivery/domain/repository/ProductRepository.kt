package com.itis.delivery.domain.repository

import com.itis.delivery.domain.model.ProductDomainModel

interface ProductRepository {

    suspend fun getProducts(page: Int) : List<ProductDomainModel>

    suspend fun getProductById(productId: Long) : ProductDomainModel?

    suspend fun getProductsByIndices(vararg productIds: Long) : List<ProductDomainModel>

    suspend fun getProductsByIndicesAndSearchTerm(
        vararg productIds: Long,
        searchTerm: String,
        page: Int
    ) : List<ProductDomainModel>

    suspend fun getProductsByCategory(categoryTag: String, page: Int) : List<ProductDomainModel>

    suspend fun getProductsBySearchTerm(searchTerm: String, page: Int) : List<ProductDomainModel>
}
