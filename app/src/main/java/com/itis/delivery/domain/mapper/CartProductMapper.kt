package com.itis.delivery.domain.mapper

import android.util.Log
import com.itis.delivery.domain.model.CartDomainModel
import com.itis.delivery.domain.model.ProductDomainModel
import com.itis.delivery.presentation.model.CartProductModel
import javax.inject.Inject

class CartProductMapper @Inject constructor() {

    fun mapToCartProductModelList(
        productList: List<ProductDomainModel>,
        cartList: List<CartDomainModel>
    ): MutableList<CartProductModel> {
        val cartProductList = mutableListOf<CartProductModel>()

        productList.forEach { product ->
            cartList.filter { product.id == it.productId }
                .mapTo(cartProductList) { cart ->
                    CartProductModel(
                        productName = product.name,
                        productImageUrl = product.imageUrl,
                        price = product.price,
                        productId = cart.productId,
                        count = cart.count
                    )
                }
        }

        Log.d("CartProductMapper", "mapToCartProductModelList.size(): ${cartProductList.size}")
        return cartProductList
    }
}