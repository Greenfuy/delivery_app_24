package com.itis.delivery.presentation.screens.search

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.itis.delivery.domain.usecase.product.GetProductListByCategoryUseCase
import com.itis.delivery.domain.usecase.product.GetProductListByPriceAndRateUseCase
import com.itis.delivery.domain.usecase.product.GetProductListBySearchTermUseCase
import com.itis.delivery.presentation.base.BaseViewModel
import com.itis.delivery.presentation.model.ProductUiModel
import com.itis.delivery.utils.ExceptionHandlerDelegate
import com.itis.delivery.utils.runSuspendCatching
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultSearchViewModel @Inject constructor(
    private val getProductListBySearchTermUseCase: GetProductListBySearchTermUseCase,
    private val getProductListByCategoryUseCase: GetProductListByCategoryUseCase,
    private val getProductListByPriceAndRateUseCase: GetProductListByPriceAndRateUseCase,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate
) : BaseViewModel() {

    private val _productList = MutableStateFlow<List<ProductUiModel>>(emptyList())
    val productList = _productList.asStateFlow()
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private var page = 1
    private var state = STATE_SEARCH_TERM
    private var searchTerm = ""
    private var categoryTag = ""
    private var rate = 0
    private var priceStart = 0
    private var priceEnd = 0
    
    private fun getProductsBySearchTerm(searchTerm: String) {
        state = STATE_SEARCH_TERM
        this.searchTerm = searchTerm
        viewModelScope.launch {
            if (page == 1) _isLoading.value = true
            runSuspendCatching(exceptionHandlerDelegate) {
                getProductListBySearchTermUseCase.invoke(searchTerm, page)
            }.onSuccess {
                _isLoading.value = false
                _productList.value = it
                page++
                Log.d("ResultSearchViewModel", "searchTerm: $searchTerm")
            }.onFailure {
                _isLoading.value = false
                exceptionHandlerDelegate.handleException(it).also { throwable ->
                    errorsChannel.send(throwable)
                }
                Log.e("ResultSearchViewModel", "Error: $it", it)
            }
        }
    }

    fun getProductsByCategory(categoryTag: String) {
        state = STATE_CATEGORY
        this.categoryTag = categoryTag
        viewModelScope.launch {
            if (page == 1) _isLoading.value = true
            runSuspendCatching(exceptionHandlerDelegate) {
                getProductListByCategoryUseCase.invoke(categoryTag, page)
            }.onSuccess {
                _productList.value = it
                _isLoading.value = false
                page++
                Log.d("ResultSearchViewModel", "categoryTag: $categoryTag")
            }.onFailure {
                _isLoading.value = false
                exceptionHandlerDelegate.handleException(it).also { throwable ->
                    errorsChannel.send(throwable)
                }
                Log.e("ResultSearchViewModel", "Error: $it", it)
            }
        }
    }

    fun getProductsByPriceAndRate(searchTerm: String, rate: Int, priceStart: Int, priceEnd: Int) {
        state = STATE_PRICE_AND_RATE
        this.searchTerm = searchTerm
        this.rate = rate
        this.priceStart = priceStart
        this.priceEnd = priceEnd
        viewModelScope.launch {
            if (page == 1) _isLoading.value = true
            runSuspendCatching(exceptionHandlerDelegate) {
                getProductListByPriceAndRateUseCase
                    .invoke(searchTerm, priceStart, priceEnd, rate, page)
            }.onSuccess {
                _productList.value = it
                _isLoading.value = false
                page++
                Log.d("ResultSearchViewModel", "rate: $rate, priceStart: $priceStart, priceEnd: $priceEnd")
            }.onFailure {
                _isLoading.value = false
                exceptionHandlerDelegate.handleException(it).also { throwable ->
                    errorsChannel.send(throwable)
                }
                Log.e("ResultSearchViewModel", "Error: $it", it)
            }
        }
    }

    fun refresh() {
        page = 1
        when (state) {
            STATE_SEARCH_TERM -> getProductsBySearchTerm(searchTerm)
            STATE_CATEGORY -> getProductsByCategory(categoryTag)
            STATE_PRICE_AND_RATE ->
                getProductsByPriceAndRate(searchTerm, rate, priceStart, priceEnd)
        }
        Log.d("ResultSearchViewModel", "refresh, state: $state")
    }

    companion object {
        private const val STATE_SEARCH_TERM = 0
        private const val STATE_CATEGORY = 1
        private const val STATE_PRICE_AND_RATE = 2
    }
}