package com.itis.delivery.data.mapper

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.itis.delivery.domain.model.UserDomainModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDomainModelMapper @Inject constructor() {

    fun firebaseUserToUserModel(input: FirebaseUser): UserDomainModel {
        Log.d("UserDomainModelMapper",
            "firebaseUserToUserModel(), uid = ${input.uid}, " +
                    "displayName = ${input.displayName}, email = ${input.email}")
        return UserDomainModel(
            uid = input.uid,
            username = input.displayName ?: "",
            email = input.email!!
        )
    }

    fun firebaseDocToUserModel(input: DocumentSnapshot): UserDomainModel =
        input.toObject(UserDomainModel::class.java)!!

}