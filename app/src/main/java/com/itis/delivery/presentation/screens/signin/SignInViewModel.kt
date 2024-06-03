package com.itis.delivery.presentation.screens.signin

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.itis.delivery.base.AuthErrors
import com.itis.delivery.domain.usecase.auth.SignInUseCase
import com.itis.delivery.domain.usecase.validation.EmailValidateUseCase
import com.itis.delivery.presentation.base.BaseViewModel
import com.itis.delivery.utils.ExceptionHandlerDelegate
import com.itis.delivery.utils.runSuspendCatching
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate,
    private val emailValidateUseCase: EmailValidateUseCase
) : BaseViewModel() {

    private val _signingIn = MutableStateFlow(false)

    private val _error = MutableStateFlow<AuthErrors?>(null)
    val error: StateFlow<AuthErrors?> get() = _error

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> get() = _success

    fun onSignUpClick(email: String, password: String) {
        if (!_signingIn.value) {
            if (!emailValidateUseCase.invoke(email) || password.isEmpty()) {
                _error.value = AuthErrors.INVALID_CREDENTIALS
            } else {
                signIn(email, password)
            }
        } else {
            Log.e("SignInViewModel", "Wait")
            _error.value = AuthErrors.WAIT
        }
    }

    private fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _signingIn.value = true
            runSuspendCatching(exceptionHandlerDelegate) {
                signInUseCase.invoke(email, password)
            }.onFailure {
                if (it is FirebaseAuthInvalidCredentialsException) {
                    Log.e("SignInViewModel", "Invalid credentials", it)
                    _error.value = AuthErrors.INVALID_CREDENTIALS
                } else {
                    Log.e("SignInViewModel", "Unexpected error", it)
                    _error.value = AuthErrors.UNEXPECTED
                }
            }.onSuccess {
                Log.d("SignInViewModel", "Successfully signed in")
                _error.value = null
                _success.value = true
            }
            _signingIn.value = false
            _error.value = null
        }
    }
}