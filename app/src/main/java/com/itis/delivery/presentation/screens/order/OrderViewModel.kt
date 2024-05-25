package com.itis.delivery.presentation.screens.order

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.itis.delivery.base.Keys
import com.itis.delivery.domain.repository.CartRepository
import com.itis.delivery.domain.repository.OrderRepository
import com.itis.delivery.domain.usecase.product.GetCartProductListUseCase
import com.itis.delivery.presentation.base.BaseViewModel
import com.itis.delivery.presentation.model.CartProductModel
import com.itis.delivery.utils.ExceptionHandlerDelegate
import com.itis.delivery.utils.runSuspendCatching
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderViewModel @AssistedInject constructor(
    @Assisted(Keys.PRODUCTS) private val products: LongArray,
    private val orderRepository: OrderRepository,
    private val cartRepository: CartRepository,
    private val getCartProductListUseCase: GetCartProductListUseCase,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate
) : BaseViewModel() {

    private val productCounts: MutableList<Long> = mutableListOf()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    private val _carOrderList = MutableStateFlow<List<CartProductModel>>(emptyList())
    val cartOrderList = _carOrderList.asStateFlow()
    private val _success = MutableStateFlow(false)
    val success = _success.asStateFlow()

    fun getCartOrderList() {
        _isLoading.value = true
        viewModelScope.launch {
            runSuspendCatching(exceptionHandlerDelegate) {
                getCartProductListUseCase.invoke(productIds = products)
            }.onSuccess {
                _carOrderList.value = it
                _isLoading.value = false
                setProductCounts(it)
                Log.d("OrderViewModel", "cartOrderList.size(): ${it.size}")
            }.onFailure {
                _isLoading.value = false
                exceptionHandlerDelegate.handleException(it).also { throwable ->
                    errorsChannel.send(throwable)
                    Log.e("OrderViewModel", "Error: $it", it)
                }
            }
        }
    }

    fun createOrder(address: String) {
        _isLoading.value = true
        viewModelScope.launch {
            runSuspendCatching(exceptionHandlerDelegate) {
                orderRepository.addOrder(
                    products = products.zip(productCounts).toMap(),
                    address = address
                )
            }.onSuccess {
                _success.value = true
                _isLoading.value = false
                cartRepository.removeAll(productIds = products)
            }.onFailure {
                _success.value = false
                _isLoading.value = false
                exceptionHandlerDelegate.handleException(it).also { throwable ->
                    errorsChannel.send(throwable)
                    Log.e("OrderViewModel", "Error: $it", it)
                }
            }
        }
    }

    private fun setProductCounts(cartProducts: List<CartProductModel>) {
        productCounts.addAll(cartProducts.map { it.count })
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted(Keys.PRODUCTS) products: LongArray): OrderViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: Factory,
            products: LongArray?
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(products = products ?: longArrayOf()) as T
            }
        }
    }
}