package com.itis.delivery.domain.mapper

import android.util.Log
import com.itis.delivery.domain.model.ProductDomainModel
import com.itis.delivery.presentation.model.NutrimentsDataUiModel
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
                categoriesTags = categoriesTags,
                labels = labels,
                imageUrl = imageUrl,
                nutriments = NutrimentsDataUiModel(
                    carbohydrates = nutriments.carbohydrates,
                    energyKcal = nutriments.energyKcal,
                    fat = nutriments.fat,
                    proteins = nutriments.proteins,
                    salt = nutriments.salt,
                    saturatedFat = nutriments.saturatedFat,
                    sugars = nutriments.sugars
                ),
                price = price
            )
        }
    }

    fun mapDomainModelListToUiModelList(input: List<ProductDomainModel>) : List<ProductUiModel> {
        Log.d("ProductUiModelMapper", "mapDomainModelListToUiModelList.size(): ${input.size}")
        return input.map { mapDomainModelToUiModel(it) }
    }
}