package com.itis.delivery.data.remote.pojo.response

import com.google.gson.annotations.SerializedName
import com.itis.delivery.base.Constants

class ProductResponse(
    val id: Long? = 0L,
    @SerializedName("product_name_en")
    val name: String?,
    @SerializedName("brands")
    val brands: String?,
    val quantity: String?,
    @SerializedName("categories_tags_en")
    val categoriesTags: List<String>?,
    @SerializedName("labels")
    val labels: String?,
    @SerializedName("image_url")
    val imageUrl: String?,
    val nutriments: NutrimentsData
)

fun ProductResponse.isNotFull() =
    id == null
            || name.isNullOrEmpty()
            || brands.isNullOrEmpty()
            || quantity.isNullOrEmpty()
            || categoriesTags.isNullOrEmpty()
            || imageUrl.isNullOrEmpty()
            || labels.isNullOrEmpty()
            || nutriments == NutrimentsData(
        carbohydrates = Constants.EMPTY_FLOAT_DATA,
        energyKcal = Constants.EMPTY_FLOAT_DATA,
        fat = Constants.EMPTY_FLOAT_DATA,
        proteins = Constants.EMPTY_FLOAT_DATA,
        salt = Constants.EMPTY_FLOAT_DATA,
        saturatedFat = Constants.EMPTY_FLOAT_DATA,
        sugars = Constants.EMPTY_FLOAT_DATA
    )

class NutrimentsData(
    val carbohydrates: Double,
    val energyKcal: Double,
    val fat: Double,
    val proteins: Double,
    val salt: Double,
    val saturatedFat: Double,
    val sugars: Double
)

class ProductListResponse(
    val products: List<ProductResponse>
)

class ShortProductRespon
