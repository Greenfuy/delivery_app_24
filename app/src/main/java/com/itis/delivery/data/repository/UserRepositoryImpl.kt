package com.itis.delivery.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.itis.delivery.data.mapper.UserDomainModelMapper
import com.itis.delivery.domain.model.UserDomainModel
import com.itis.delivery.domain.repository.UserRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val mapper: UserDomainModelMapper
) : UserRepository {

    override suspend fun signUp(
        username: String,
        email: String,
        password: String
    ): UserDomainModel {
        Log.i("FirebaseUserRepository",
            "SignUp(username=$username, email=$email, password=$password)")
        val user = createUserWithEmailAndPassword(email, password).copy(username = username)
        saveUserToStore(user)
        return user
    }

    override suspend fun signIn(email: String, password: String): UserDomainModel {
        Log.i("FirebaseUserRepository", "SignIn(email=$email, password=$password)")
        val userFB = auth.signInWithEmailAndPassword(email, password).await()
        return mapper.firebaseUserToUserModel(userFB.user!!)
    }

    private suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): UserDomainModel {
        val userFB = auth.createUserWithEmailAndPassword(email, password).await()
        return mapper.firebaseUserToUserModel(userFB.user!!)
    }

    private suspend fun saveUserToStore(user: UserDomainModel) {
        db.collection("users").document(user.uid).set(user).await()
    }

    override suspend fun getUserById(userId: String): UserDomainModel = mapper.firebaseDocToUserModel(
        db.collection("users").document(userId).get().await()
    )

}