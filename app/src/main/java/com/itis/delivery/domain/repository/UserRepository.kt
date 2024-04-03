package com.itis.delivery.domain.repository

import com.itis.delivery.domain.model.UserModel

interface UserRepository {
    suspend fun signUp(
        username: String,
        email: String,
        password: String
    ): UserModel
    suspend fun signIn(email: String, password: String): UserModel
    suspend fun getUserById(userId: String): UserModel
}