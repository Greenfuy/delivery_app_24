package com.itis.delivery.domain.usecase.validation

import com.itis.delivery.base.Regexes
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmailValidateUseCase @Inject constructor() {
    operator fun invoke(email: String): Boolean = email.matches(Regexes.emailRegex)
}