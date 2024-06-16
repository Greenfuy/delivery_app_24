package com.itis.delivery.domain.usecase.product

import com.itis.delivery.domain.mapper.ProductUiModelMapper
import com.itis.delivery.domain.repository.ProductRepository
import com.itis.delivery.domain.repository.RateRepository
import com.itis.delivery.presentation.model.ProductUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetProductListByPriceAndRateUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val rateRepository: RateRepository,
    private val mapper: ProductUiModelMapper,
    private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(
        searchTerm: String,
        priceStart: Int,
        priceEnd: Int,
        rate: Int,
        page: Int
    ): List<ProductUiModel> {
        if (searchTerm.isEmpty() || rate < 0 || priceStart < 0 || priceEnd < 0) {
            throw IllegalArgumentException("Wrong sort data")
        }

        return withContext(dispatcher) {
            val products = if (rate > 0) {
                val productIds = rateRepository.getProductIndicesByRate(rate).toLongArray()
                productRepository
                    .getProductsByIndicesAndSearchTerm(productIds = productIds, searchTerm, page)
            } else {
                productRepository.getProductsBySearchTerm(searchTerm, page)
            }

            val filteredProducts = products.filter { product ->
                when {
                    priceEnd > 0 && priceStart <= priceEnd ->
                        product.price in priceStart..priceEnd
                    priceEnd == 0 && priceStart > 0 -> product.price > priceStart
                    priceStart == 0 -> true
                    else -> false
                }
            }

            mapper.mapDomainModelListToUiModelList(filteredProducts)
        }
    }

}