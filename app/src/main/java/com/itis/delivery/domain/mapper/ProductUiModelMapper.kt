package com.itis.delivery.domain.mapper

import android.util.Log
import com.itis.delivery.domain.model.ProductDomainModel
import com.itis.delivery.presentation.model.ProductUiModel
import javax.inject.Inject

class ProductUiModelMapper @Inject constructor() {

    fun mapDomainModelToUiModel(input: ProductDomainModel) : ProductUiModel {
        with(input) {
            return ProductUiModel(
                id = id,
                name = "$name - $quantity",
                brands = brands,
                quantity = quantity,
                imageUrl = imageUrl,
                price = price
            )
        }
    }

    fun mapDomainModelListToUiModelList(input: List<ProductDomainModel>) : List<ProductUiModel> {
        Log.d("ProductUiModelMapper", "mapDomainModelListToUiModelList.size(): ${input.size}")
        return input.map { mapDomainModelToUiModel(it) }
    }
}