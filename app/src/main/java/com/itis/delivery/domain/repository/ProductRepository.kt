package com.itis.delivery.domain.repository

import com.itis.delivery.domain.model.ProductDomainModel

interface ProductRepository {

    suspend fun getProducts() : List<ProductDomainModel>

    suspend fun getProductById(productId: String) : ProductDomainModel?

    suspend fun getProductsByCategory(categoryTag: String) : List<ProductDomainModel>

    suspend fun getProductsBySearchTerm(searchTerm: String) : List<ProductDomainModel>
}
