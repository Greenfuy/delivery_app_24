package com.itis.delivery.domain.repository

import com.itis.delivery.domain.model.UserDomainModel

interface UserRepository {
    suspend fun signUp(
        username: String,
        email: String,
        password: String
    ): UserDomainModel

    suspend fun signIn(email: String, password: String): UserDomainModel

    suspend fun getUserById(userId: String): UserDomainModel

    suspend fun getCurrentUserId(): String?

    suspend fun getCurrentUserCredentials(): String

    suspend fun updateUserCredentials(username: String) : Boolean

    suspend fun signOut()
}