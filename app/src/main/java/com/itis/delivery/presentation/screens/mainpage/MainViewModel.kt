package com.itis.delivery.presentation.screens.mainpage

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.itis.delivery.domain.repository.CategoryRepository
import com.itis.delivery.domain.usecase.product.GetProductListUseCase
import com.itis.delivery.presentation.base.BaseViewModel
import com.itis.delivery.presentation.model.CategoryUiModel
import com.itis.delivery.presentation.model.ProductUiModel
import com.itis.delivery.utils.ExceptionHandlerDelegate
import com.itis.delivery.utils.runSuspendCatching
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getProductListUseCase: GetProductListUseCase,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate,
    private val categoryRepository: CategoryRepository
) : BaseViewModel() {

    private val _productList = MutableStateFlow<List<ProductUiModel>>(emptyList())
    val productList = _productList.asStateFlow()

    // TODO: create a more optimal way to receive categories
    var categoryList = emptyList<CategoryUiModel>()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        categoryList = categoryRepository.getCategories()
        setProducts()
    }

    private fun setProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            runSuspendCatching(exceptionHandlerDelegate = exceptionHandlerDelegate) {
                getProductListUseCase.invoke()
            }.onSuccess {
                _productList.value = it
                Log.d("MainViewModel", "productList.size(): ${it.size}")
            }.onFailure {
                exceptionHandlerDelegate.handleException(it).also { throwable ->
                    errorsChannel.send(throwable)
                }
                Log.e("MainViewModel", "Error: $it", it)
            }
            _isLoading.value = false
        }
    }

    fun refresh() {
        categoryList = categoryRepository.getCategories()
        setProducts()
    }
}