package com.itis.delivery.domain.usecase.signin

import com.itis.delivery.domain.mapper.UserUiModelMapper
import com.itis.delivery.domain.repository.UserRepository
import com.itis.delivery.presentation.model.UserUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignInUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val dispatcher: CoroutineDispatcher,
    private val mapper: UserUiModelMapper
) {
    suspend operator fun invoke(email: String, password: String): UserUiModel {
        return withContext(dispatcher) {
            mapper.mapDomainToUiModel(input = userRepository.signIn(email, password))
        }
    }
}