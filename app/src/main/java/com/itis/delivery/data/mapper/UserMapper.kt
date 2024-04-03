package com.itis.delivery.data.mapper

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.itis.delivery.domain.model.UserModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserMapper @Inject constructor() {

    fun firebaseUserToUserModel(firebaseUser: FirebaseUser): UserModel {
        Log.d("UserMapper",
            "firebaseUserToUserModel(), uid = ${firebaseUser.uid}, " +
                    "displayName = ${firebaseUser.displayName}, email = ${firebaseUser.email}")
        return UserModel(
            uid = firebaseUser.uid,
            username = firebaseUser.displayName!!,
            email = firebaseUser.email!!
        )
    }

    fun firebaseDocToUserModel(document: DocumentSnapshot): UserModel =
        document.toObject(UserModel::class.java)!!

}