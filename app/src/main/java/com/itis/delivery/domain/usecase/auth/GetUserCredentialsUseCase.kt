package com.itis.delivery.domain.usecase.auth

import com.itis.delivery.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetUserCredentialsUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val dispatcher: CoroutineDispatcher,
) {

    suspend operator fun invoke() : String {
        return withContext(dispatcher) {
            userRepository.getCurrentUserCredentials()
        }
    }
}