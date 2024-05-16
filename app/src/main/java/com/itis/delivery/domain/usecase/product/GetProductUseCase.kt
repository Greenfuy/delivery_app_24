package com.itis.delivery.domain.usecase.product

import android.util.Log
import com.itis.delivery.domain.mapper.ProductUiModelMapper
import com.itis.delivery.domain.repository.ProductRepository
import com.itis.delivery.presentation.model.ProductUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetProductUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val dispatcher: CoroutineDispatcher,
    private val mapper: ProductUiModelMapper
) {

    suspend operator fun invoke(id: Long) : ProductUiModel {
        return withContext(dispatcher) {
            Log.d("GetProductUseCase", "id: $id")
            val domainModel = productRepository.getProductById(id)
            if (domainModel != null) mapper.mapDomainModelToUiModel(domainModel)
            else throw NullPointerException("Product not found")
        }
    }
}