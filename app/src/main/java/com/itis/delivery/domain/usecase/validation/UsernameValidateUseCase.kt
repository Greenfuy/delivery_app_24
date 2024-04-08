package com.itis.delivery.domain.usecase.validation

import com.itis.delivery.utils.Regexes
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsernameValidateUseCase @Inject constructor() {
    operator fun invoke(name: String): Boolean = name.matches(Regexes.nameRegex)
}