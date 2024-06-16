package com.itis.delivery.domain.usecase.product

import com.itis.delivery.domain.mapper.ProductUiModelMapper
import com.itis.delivery.domain.repository.ProductRepository
import com.itis.delivery.presentation.model.ProductUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetProductListByCategoryUseCase @Inject constructor(
    private val repository: ProductRepository,
    private val mapper: ProductUiModelMapper,
    private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(categoryTag: String, page: Int): List<ProductUiModel> =
        withContext(dispatcher) {
            mapper.mapDomainModelListToUiModelList(
                repository.getProductsByCategory(categoryTag, page)
            )
        }
}