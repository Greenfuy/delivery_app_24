package com.itis.delivery.domain.usecase.product

import com.itis.delivery.domain.mapper.ProductUiModelMapper
import com.itis.delivery.domain.repository.ProductRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetProductListBySearchTermUseCase @Inject constructor(
    private val repository: ProductRepository,
    private val mapper: ProductUiModelMapper,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(searchTerm: String, page: Int) =
        withContext(dispatcher) {
            mapper.mapDomainModelListToUiModelList(
                repository.getProductsBySearchTerm(searchTerm, page)
            )
        }
}