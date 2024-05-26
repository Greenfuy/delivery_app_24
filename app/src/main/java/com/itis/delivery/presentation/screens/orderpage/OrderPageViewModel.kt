package com.itis.delivery.presentation.screens.orderpage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.itis.delivery.domain.repository.RateRepository
import com.itis.delivery.domain.usecase.order.CancelOrderUseCase
import com.itis.delivery.domain.usecase.order.GetOrderUseCase
import com.itis.delivery.presentation.base.BaseViewModel
import com.itis.delivery.presentation.model.OrderUiModel
import com.itis.delivery.utils.ExceptionHandlerDelegate
import com.itis.delivery.utils.runSuspendCatching
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderPageViewModel @AssistedInject constructor(
    @Assisted private val orderNumber: Long,
    private val rateRepository: RateRepository,
    private val getOrderUseCase: GetOrderUseCase,
    private val cancelOrderUseCase: CancelOrderUseCase,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate
) : BaseViewModel() {

    private val _order = MutableStateFlow<OrderUiModel?>(null)
    val order = _order.asStateFlow()
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()
    private val _isOrderCancelled = MutableStateFlow(false)
    val isOrderCancelled = _isOrderCancelled.asStateFlow()
    private val _isRated = MutableStateFlow(false)
    val isRated = _isRated.asStateFlow()

    fun getOrder() {
        viewModelScope.launch {
            _isLoading.value = true
            runSuspendCatching(exceptionHandlerDelegate = exceptionHandlerDelegate) {
                getOrderUseCase.invoke(orderNumber)
            }.onSuccess {
                _isLoading.value = false
                _order.value = it
                Log.d("OrderPageViewModel", "order: $it")
            }.onFailure {
                _isLoading.value = false
                exceptionHandlerDelegate.handleException(it).also { throwable ->
                    errorsChannel.send(throwable)
                }
                Log.e("OrderPageViewModel", "Error: $it", it)
            }
        }
    }

    fun cancelOrder() {
        viewModelScope.launch {
            runSuspendCatching(exceptionHandlerDelegate = exceptionHandlerDelegate) {
                cancelOrderUseCase.invoke(orderNumber)
            }.onSuccess {
                _isOrderCancelled.value = it
                Log.d("OrderPageViewModel", "cancelled: $it")
            }.onFailure {
                exceptionHandlerDelegate.handleException(it).also { throwable ->
                    errorsChannel.send(throwable)
                }
                Log.e("OrderPageViewModel", "Error: $it", it)
            }
        }
    }

    fun rateProduct(productId: Long, rate: Int) {
        viewModelScope.launch {
            runSuspendCatching(exceptionHandlerDelegate = exceptionHandlerDelegate) {
                rateRepository.addRate(productId, rate)
            }.onSuccess {
                _isRated.value = true
                Log.d("OrderPageViewModel", "rated: $it")
            }.onFailure {
                _isLoading.value = false
                exceptionHandlerDelegate.handleException(it).also { throwable ->
                    errorsChannel.send(throwable)
                }
                Log.e("OrderPageViewModel", "Error: $it", it)
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(orderNumber: Long): OrderPageViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: Factory,
            orderNumber: Long
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(orderNumber) as T
            }
        }
    }
}