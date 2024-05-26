package com.itis.delivery.presentation.screens.profile

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.itis.delivery.domain.usecase.auth.ChangeUserCredentialsUseCase
import com.itis.delivery.domain.usecase.auth.GetUserCredentialsUseCase
import com.itis.delivery.presentation.base.BaseViewModel
import com.itis.delivery.utils.ExceptionHandlerDelegate
import com.itis.delivery.utils.runSuspendCatching
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserCredentialsUseCase: GetUserCredentialsUseCase,
    private val changeUserCredentialsUseCase: ChangeUserCredentialsUseCase,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate
) : BaseViewModel() {

    private val _credentials = MutableStateFlow<String?>(null)
    val credentials = _credentials.asStateFlow()
    private val _isCredentialsUpdated = MutableStateFlow(false)
    val isCredentialsUpdated = _isCredentialsUpdated.asStateFlow()

    fun getUserCredentials() {
        viewModelScope.launch {
            runSuspendCatching(exceptionHandlerDelegate) {
                getUserCredentialsUseCase.invoke()
            }.onSuccess {
                _credentials.value = it
                Log.d("ProfileViewModel", "getUserCredentials: $it")
            }.onFailure {
                exceptionHandlerDelegate.handleException(it).also { throwable ->
                    errorsChannel.send(throwable)
                }
                Log.e("ProfileViewModel", "getUserCredentials: $it")
            }
        }
    }

    fun changeUserCredentials(username: String) {
        viewModelScope.launch {
            runSuspendCatching(exceptionHandlerDelegate) {
                changeUserCredentialsUseCase.invoke(username)
            }.onSuccess {
                _isCredentialsUpdated.value = it
                getUserCredentials()
                Log.d("ProfileViewModel", "changeUserCredentials: $it")
            }.onFailure {
                exceptionHandlerDelegate.handleException(it).also { throwable ->
                    errorsChannel.send(throwable)
                }
                Log.e("ProfileViewModel", "changeUserCredentials: $it")
            }
        }
    }
}