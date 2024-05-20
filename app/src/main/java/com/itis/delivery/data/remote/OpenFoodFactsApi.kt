package com.itis.delivery.data.remote

import com.itis.delivery.data.remote.pojo.response.ProductListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenFoodFactsApi {

    @GET("search.pl")
    suspend fun getProducts(
        @Query(value = "page") page: Int
    ): ProductListResponse?

    @GET("search.pl")
    suspend fun getProductsBySearchTerm(
        @Query(value = "search_terms") searchTerm: String,
        @Query(value = "page") page: Int
    ): ProductListResponse?

    @GET("search.pl")
    suspend fun getProductsByCategory(
        @Query(value = "categories_tags") categoryTag: String,
        @Query(value = "page") page: Int
    ): ProductListResponse?

    @GET("search.pl")
    suspend fun getProductById(
        @Query(value = "code") code: String
    ): ProductListResponse?

    @GET("search.pl")
    suspend fun getProductsByIndices(
        @Query(value = "code") code: String,
        @Query(value = "page") page: Int
    ): ProductListResponse?

    @GET("search.pl")
    suspend fun getProductsByIndicesAndSearchTerm(
        @Query(value = "search_terms") searchTerm: String,
        @Query(value = "code") code: String,
        @Query(value = "page") page: Int
    ): ProductListResponse?
}