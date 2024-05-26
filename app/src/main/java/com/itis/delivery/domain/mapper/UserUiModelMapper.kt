package com.itis.delivery.domain.mapper

import android.util.Log
import com.itis.delivery.domain.model.UserDomainModel
import com.itis.delivery.presentation.model.UserUiModel
import javax.inject.Inject

class UserUiModelMapper @Inject constructor() {

    fun mapDomainToUiModel(input: UserDomainModel): UserUiModel {
        with(input) {
            Log.d("UserUiModelMapper", "mapDomainToUiModel: $uid, $username, $email")
            return UserUiModel(
                username = username,
                email = email
            )
        }
    }
}