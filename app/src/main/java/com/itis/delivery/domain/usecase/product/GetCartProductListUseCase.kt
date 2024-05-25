package com.itis.delivery.domain.usecase.product

import com.itis.delivery.data.exceptions.UserNotAuthorizedException
import com.itis.delivery.domain.mapper.CartProductMapper
import com.itis.delivery.domain.repository.CartRepository
import com.itis.delivery.domain.repository.ProductRepository
import com.itis.delivery.presentation.model.CartProductModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetCartProductListUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
    private val mapper: CartProductMapper,
    private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(vararg productIds: Long = longArrayOf()): List<CartProductModel> {
        return withContext(dispatcher) {
            val cartList = if (productIds.isEmpty()) {
                cartRepository.getCartList()
            } else {
                cartRepository.getCartListByProductIndices(productIds = productIds)
            }
            val productIndices = cartList.map { it.productId }
            val productList = productRepository
                .getProductsByIndices(productIds = productIndices.toLongArray())

            mapper.mapToCartProductModelList(productList = productList, cartList = cartList)
        }
    }
}