package com.itis.delivery.presentation.screens.settings

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.itis.delivery.domain.usecase.auth.GetUserCredentialsUseCase
import com.itis.delivery.domain.usecase.auth.SignOutUseCase
import com.itis.delivery.domain.usecase.partnerscard.GetPartnersCardQrCodeUseCase
import com.itis.delivery.presentation.base.BaseViewModel
import com.itis.delivery.utils.ExceptionHandlerDelegate
import com.itis.delivery.utils.runSuspendCatching
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getUserCredentialsUseCase: GetUserCredentialsUseCase,
    private val getPartnersCardQrCodeUseCase: GetPartnersCardQrCodeUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate
) : BaseViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    private val _userCredentials = MutableStateFlow<String?>(null)
    val userCredentials = _userCredentials.asStateFlow()
    private val _qrCode = MutableStateFlow<Pair<Long, Bitmap>?>(null)
    val qrCode = _qrCode.asStateFlow()
    private val _signOut = MutableStateFlow(false)
    val signOut = _signOut.asStateFlow()


    fun getUserCredentials() {
        viewModelScope.launch {
            _isLoading.value = true
            runSuspendCatching(exceptionHandlerDelegate) {
                getUserCredentialsUseCase.invoke()
            }.onSuccess {
                _userCredentials.value = it
                _isLoading.value = false
                Log.d("SettingsViewModel", "User credentials: $it")
            }.onFailure {
                _isLoading.value = false
                exceptionHandlerDelegate.handleException(it).also { throwable ->
                    errorsChannel.send(throwable)
                }
                Log.e("SettingsViewModel", "Error: $it", it)
            }
        }
    }

    fun getQrCode() {
        viewModelScope.launch {
            runSuspendCatching(exceptionHandlerDelegate) {
                getPartnersCardQrCodeUseCase.invoke()
            }.onSuccess {
                _qrCode.value = it
                Log.d("SettingsViewModel", "QR code: $it")
            }.onFailure {
                exceptionHandlerDelegate.handleException(it).also { throwable ->
                    errorsChannel.send(throwable)
                }
                Log.e("SettingsViewModel", "Error: $it", it)
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            runSuspendCatching(exceptionHandlerDelegate) {
                signOutUseCase.invoke()
            }.onSuccess {
                _signOut.value = true
                Log.d("SettingsViewModel", "Successfully signed out")
            }.onFailure {
                _signOut.value = false
                exceptionHandlerDelegate.handleException(it).also { throwable ->
                    errorsChannel.send(throwable)
                }
                Log.e("SettingsViewModel", "Error: $it", it)
            }
        }
    }
}