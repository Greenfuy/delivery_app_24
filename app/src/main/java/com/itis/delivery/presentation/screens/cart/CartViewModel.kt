package com.itis.delivery.presentation.screens.cart

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.itis.delivery.domain.repository.CartRepository
import com.itis.delivery.domain.usecase.cart.GetCartProductListUseCase
import com.itis.delivery.presentation.base.BaseViewModel
import com.itis.delivery.presentation.model.CartProductModel
import com.itis.delivery.utils.ExceptionHandlerDelegate
import com.itis.delivery.utils.runSuspendCatching
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val getCartProductListUseCase: GetCartProductListUseCase,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate,
) : BaseViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()
    private val _cartProductList = MutableStateFlow<List<CartProductModel>>(emptyList())
    val cartProductList = _cartProductList.asStateFlow()
    private val _isRemoved = MutableStateFlow(false)
    val isRemoved = _isRemoved.asStateFlow()

    fun getCartProductList() {
        _isLoading.value = true
        viewModelScope.launch {
            runSuspendCatching(exceptionHandlerDelegate) {
                getCartProductListUseCase.invoke()
            }.onSuccess {
                _cartProductList.value = it
                _isLoading.value = false
                Log.d("CartViewModel", "cartProductList.size(): ${it.size}")
            }.onFailure {
                _isLoading.value = false
                exceptionHandlerDelegate.handleException(it).also { throwable ->
                    errorsChannel.send(throwable)
                    Log.e("CartViewModel", "Error: $it", it)
                }
            }
        }
    }

    fun removeAllFromCart(vararg productIds: Long): Boolean {
        _isLoading.value = true
        viewModelScope.launch {
            runSuspendCatching(exceptionHandlerDelegate) {
                cartRepository.removeAll(productIds = productIds)
            }.onSuccess {
                getCartProductList()
                _isRemoved.value = true
                _isLoading.value = false
                Log.d("CartViewModel", "Removed from cart: ${productIds.size}")
            }.onFailure {
                _isRemoved.value = false
                _isLoading.value = false
                exceptionHandlerDelegate.handleException(it).also { throwable ->
                    errorsChannel.send(throwable)
                    Log.e("CartViewModel", "Error: $it", it)
                }
            }
        }
        return true
    }

    fun refresh() {
        getCartProductList()
    }
}