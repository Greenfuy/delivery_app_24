package com.itis.delivery.domain.usecase.auth

import com.itis.delivery.domain.mapper.UserUiModelMapper
import com.itis.delivery.domain.repository.UserRepository
import com.itis.delivery.presentation.model.UserUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignUpUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val dispatcher: CoroutineDispatcher,
    private val mapper: UserUiModelMapper
) {
    suspend operator fun invoke(username: String,email: String, password: String): UserUiModel {
        return withContext(dispatcher) {
            mapper.mapDomainToUiModel(input = userRepository.signUp(username, email, password))
        }
    }
}