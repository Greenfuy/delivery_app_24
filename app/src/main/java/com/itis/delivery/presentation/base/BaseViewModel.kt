package com.itis.delivery.presentation.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel

abstract class BaseViewModel : ViewModel() {
    val errorsChannel = Channel<Throwable>()

    override fun onCleared() {
        errorsChannel.close()
        super.onCleared()
    }
}