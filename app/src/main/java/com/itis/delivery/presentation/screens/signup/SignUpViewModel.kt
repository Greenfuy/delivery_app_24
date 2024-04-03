package com.itis.delivery.presentation.screens.signup

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.itis.delivery.domain.usecase.signup.SignUpUseCase
import com.itis.delivery.domain.usecase.validation.EmailValidateUseCase
import com.itis.delivery.domain.usecase.validation.UsernameValidateUseCase
import com.itis.delivery.domain.usecase.validation.PasswordValidateUseCase
import com.itis.delivery.domain.usecase.validation.PasswordsMatchUseCase
import com.itis.delivery.presentation.base.BaseViewModel
import com.itis.delivery.utils.AuthErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val emailValidateUseCase: EmailValidateUseCase,
    private val usernameValidateUseCase: UsernameValidateUseCase,
    private val passwordValidateUseCase: PasswordValidateUseCase,
    private val passwordsMatchUseCase: PasswordsMatchUseCase
) : BaseViewModel() {

    private val _signingUp = MutableStateFlow(false)

    private val _error = MutableStateFlow<AuthErrors?>(null)
    val error: StateFlow<AuthErrors?> get() = _error

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> get() = _success

    fun onSignUpClick(username: String, email: String, password: String, confirmPassword: String) {
        if (isValidData(username, email, password, confirmPassword)) {
            if (!_signingUp.value) {
                signUp(username, email, password)
            } else {
                Log.d("SignUpViewModel", "Wait")
                _error.value = AuthErrors.WAIT
            }
        } else {
            Log.d("SignUpViewModel", "Invalid data")
            _error.value = AuthErrors.INVALID_DATA
        }
    }

    private fun isValidData(
        username: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        return usernameValidateUseCase.invoke(username)
                && emailValidateUseCase.invoke(email)
                && passwordValidateUseCase.invoke(password)
                && passwordsMatchUseCase.invoke(password, confirmPassword)
    }

    private fun signUp(username: String, email: String, password: String) {
        viewModelScope.launch {
            _signingUp.value = true
            val result = signUpUseCase.invoke(username, email, password)
            result.onFailure {
                if (it is FirebaseAuthUserCollisionException) {
                    Log.e("SignUpViewModel", "User already exists", it)
                    _error.value = AuthErrors.USER_ALREADY_EXISTS
                } else {
                    Log.e("SignUpViewModel", "Unexpected error", it)
                    _error.value = AuthErrors.UNEXPECTED
                }
            }.onSuccess {
                Log.d("SignUpViewModel", "Successfully signed up")
                _success.value = true
            }
            _signingUp.value = false
        }
    }
}