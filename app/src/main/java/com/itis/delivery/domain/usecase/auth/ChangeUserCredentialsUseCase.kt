package com.itis.delivery.domain.usecase.auth

import com.itis.delivery.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChangeUserCredentialsUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(username: String) : Boolean {
        return withContext(dispatcher) {
            userRepository.updateUserCredentials(username)
        }
    }
}