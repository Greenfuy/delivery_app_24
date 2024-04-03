package com.itis.delivery.domain.usecase.signin

import com.itis.delivery.domain.model.UserModel
import com.itis.delivery.domain.repository.UserRepository
import com.itis.delivery.utils.runSuspendCatching
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignInUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<UserModel> {
        return runSuspendCatching {
            userRepository.signIn(email, password)
        }
    }
}