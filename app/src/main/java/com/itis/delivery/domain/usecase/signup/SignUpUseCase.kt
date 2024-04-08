package com.itis.delivery.domain.usecase.signup

import android.util.Log
import com.itis.delivery.domain.model.UserModel
import com.itis.delivery.domain.repository.UserRepository
import com.itis.delivery.utils.runSuspendCatching
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignUpUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(username: String,email: String, password: String): Result<UserModel> {
        return runSuspendCatching {
            Log.d("SignUp", "before repository")
            userRepository.signUp(username, email, password)
        }
    }
}