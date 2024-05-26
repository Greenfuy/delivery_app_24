package com.itis.delivery.presentation.screens.orderhistory

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.itis.delivery.domain.usecase.order.GetOrderUiModelListUseCase
import com.itis.delivery.presentation.base.BaseViewModel
import com.itis.delivery.presentation.model.OrderUiModel
import com.itis.delivery.utils.ExceptionHandlerDelegate
import com.itis.delivery.utils.runSuspendCatching
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val getOrderUiModelListUseCase: GetOrderUiModelListUseCase,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate
) : BaseViewModel() {

    private val _orderList = MutableStateFlow<List<OrderUiModel>>(emptyList())
    val orderList = _orderList.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun getOrderList() {
        viewModelScope.launch {
            _isLoading.value = true
            runSuspendCatching(exceptionHandlerDelegate) {
                getOrderUiModelListUseCase.invoke()
            }.onSuccess {
                _isLoading.value = false
                _orderList.value = it
                Log.d("OrderHistoryViewModel", "orderList.size(): ${it.size}")
            }.onFailure {
                _isLoading.value = false
                exceptionHandlerDelegate.handleException(it).also { throwable ->
                    errorsChannel.send(throwable)
                    Log.e("OrderHistoryViewModel", "Error: $it", it)
                }
            }
        }
    }
}