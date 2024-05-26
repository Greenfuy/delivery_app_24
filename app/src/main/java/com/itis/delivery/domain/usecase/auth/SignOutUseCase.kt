package com.itis.delivery.domain.usecase.auth

import com.itis.delivery.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val dispatcher: CoroutineDispatcher,
) {

    suspend operator fun invoke() {
        withContext(dispatcher) {
            userRepository.signOut()
        }
    }
}