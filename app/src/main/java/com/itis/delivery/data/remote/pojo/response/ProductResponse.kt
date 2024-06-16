package com.itis.delivery.data.remote.pojo.response

import com.google.gson.annotations.SerializedName

class ProductResponse(
    val id: Long? = 0L,
    @SerializedName("product_name_en")
    val name: String?,
    @SerializedName("brands")
    val brands: String?,
    val quantity: String?,
    @SerializedName("image_url")
    val imageUrl: String?,

)

fun ProductResponse.isNotFull() =
    id == null
            || name.isNullOrEmpty()
            || brands.isNullOrEmpty()
            || quantity.isNullOrEmpty()
            || imageUrl.isNullOrEmpty()


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