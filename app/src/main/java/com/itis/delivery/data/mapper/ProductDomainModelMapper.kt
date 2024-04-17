package com.itis.delivery.data.mapper

import android.util.Log
import com.itis.delivery.data.remote.pojo.response.ProductListResponse
import com.itis.delivery.data.remote.pojo.response.ProductResponse
import com.itis.delivery.data.remote.pojo.response.isNotFull
import com.itis.delivery.domain.model.NutrimentsDataDomainModel
import com.itis.delivery.domain.model.ProductDomainModel
import javax.inject.Inject

class ProductDomainModelMapper @Inject constructor() {

    fun mapResponseToDomainModel(input: ProductResponse): ProductDomainModel? {
        with(input) {
            if (input.isNotFull().not()) {
                return ProductDomainModel(
                    id = id!!,
                    name = name!!,
                    brands = brands!!,
                    quantity = quantity!!,
                    categoriesTags = categoriesTags!!,
                    labels = labels!!,
                    imageUrl = imageUrl!!,
                    nutriments = NutrimentsDataDomainModel(
                        carbohydrates = nutriments.carbohydrates,
                        energyKcal = nutriments.energyKcal,
                        fat = nutriments.fat,
                        proteins = nutriments.proteins,
                        salt = nutriments.salt,
                        saturatedFat = nutriments.saturatedFat,
                        sugars = nutriments.sugars
                    )
                )
            }
            return null
        }
    }

    fun mapResponseToDomainModelList(input: ProductListResponse): List<ProductDomainModel> {
        with(input) {
            Log.d("ProductDomainModelMapper", "mapResponseToDomainModelList.size(): ${products.size}")
            val list = mutableListOf<ProductDomainModel>()
            products.forEach {
                val product = mapResponseToDomainModel(it)
                if (product != null) {
                    list.add(product)
                }
            }
            return list
        }
    }

}