package com.itis.delivery.domain.usecase.validation

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PasswordsMatchUseCase @Inject constructor() {
    operator fun invoke(password: String, confirmPassword: String): Boolean =
        confirmPassword == password
}