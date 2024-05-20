package com.itis.delivery.domain.usecase.validation

import com.itis.delivery.base.Regexes
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PasswordValidateUseCase @Inject constructor() {
    operator fun invoke(password: String): Boolean = password.matches(Regexes.passwordRegex)
}