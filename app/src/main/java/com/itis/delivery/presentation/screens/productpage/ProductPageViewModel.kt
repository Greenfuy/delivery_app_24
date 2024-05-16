package com.itis.delivery.presentation.screens.productpage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.itis.delivery.domain.repository.CartRepository
import com.itis.delivery.domain.repository.RateRepository
import com.itis.delivery.domain.usecase.auth.GetCurrentUserIdUseCase
import com.itis.delivery.domain.usecase.product.GetProductUseCase
import com.itis.delivery.presentation.base.BaseViewModel
import com.itis.delivery.presentation.model.ProductUiModel
import com.itis.delivery.utils.ExceptionHandlerDelegate
import com.itis.delivery.utils.runSuspendCatching
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductPageViewModel @AssistedInject constructor(
    @Assisted private val productId: Long,
    private val cartRepository: CartRepository,
    private val rateRepository: RateRepository,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate,
    private val getProductUseCase: GetProductUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase
) : BaseViewModel() {

    private val _inCartCount = MutableStateFlow(0L)
    val inCartCount = _inCartCount.asStateFlow()
    private val _product = MutableStateFlow<ProductUiModel?>(null)
    val product = _product.asStateFlow()
    private val _rate = MutableStateFlow(-1.0)
    val rate = _rate.asStateFlow()

    private var userId: String? = null


    init {
        getRate()
        getProduct()
        getCurrentUserId()
    }

    private fun getCurrentUserId() {
        viewModelScope.launch {
            runSuspendCatching(exceptionHandlerDelegate) {
                getCurrentUserIdUseCase.invoke()
            }.onSuccess {
                userId = it
                Log.d("ProductPageViewModel", "userId: $it")
                getInCartCount()
            }
        }
    }

    fun addToCart() : Boolean {
        if (userId == null) return false
        viewModelScope.launch {
            runSuspendCatching(exceptionHandlerDelegate = exceptionHandlerDelegate) {
                cartRepository.addToCart(userId!!, productId)
            }.onSuccess {
                getInCartCount()
                Log.d("ProductPageViewModel", "Added to cart")
            }.onFailure {
                exceptionHandlerDelegate.handleException(it).also { throwable ->
                    errorsChannel.send(throwable)
                }
                Log.e("ProductPageViewModel", "Error: $it", it)
            }
        }
        return true
    }

    fun removeFromCart() : Boolean {
        if (userId == null) return false
        viewModelScope.launch {
            runSuspendCatching(exceptionHandlerDelegate = exceptionHandlerDelegate) {
                cartRepository.removeFromCart(userId!!, productId)
            }.onSuccess {
                getInCartCount()
                Log.d("ProductPageViewModel", "Removed from cart")
            }.onFailure {
                exceptionHandlerDelegate.handleException(it).also { throwable ->
                    errorsChannel.send(throwable)
                }
                Log.e("ProductPageViewModel", "Error: $it", it)
            }
        }
        return true
    }

    private fun getInCartCount() {
        if (userId == null) return
        viewModelScope.launch {
            runSuspendCatching(exceptionHandlerDelegate = exceptionHandlerDelegate) {
                cartRepository.getInCartCount(userId!!, productId)
            }.onSuccess {
                _inCartCount.value = it
                Log.d("ProductPageViewModel", "inCartCount: $it")
            }.onFailure {
                exceptionHandlerDelegate.handleException(it).also { throwable ->
                    errorsChannel.send(throwable)
                }
                Log.e("ProductPageViewModel", "Error: $it", it)
            }
        }
    }

    private fun getProduct() {
        viewModelScope.launch {
            runSuspendCatching(exceptionHandlerDelegate = exceptionHandlerDelegate) {
                getProductUseCase.invoke(productId)
            }.onSuccess {
                Log.d("ProductPageViewModel", "product: $it")
                _product.value = it
            }.onFailure {
                exceptionHandlerDelegate.handleException(it).also { throwable ->
                    errorsChannel.send(throwable)
                }
                Log.e("ProductPageViewModel", "Error: $it", it)
            }
        }
    }

    private fun getRate() {
        viewModelScope.launch {
            runSuspendCatching(exceptionHandlerDelegate = exceptionHandlerDelegate) {
                rateRepository.getRateAvgByProductId(productId)
            }.onSuccess {
                _rate.value = it
                Log.d("ProductPageViewModel", "rate: $it")
            }.onFailure {
                exceptionHandlerDelegate.handleException(it).also { throwable ->
                    errorsChannel.send(throwable)
                }
                Log.e("ProductPageViewModel", "Error: $it", it)
            }
        }
    }

    fun refresh() {
        getRate()
        getProduct()
        getCurrentUserId()
    }

    @AssistedFactory
    interface Factory {
        fun create(productId: Long): ProductPageViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: Factory,
            productId: Long
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(productId) as T
            }
        }
    }
}