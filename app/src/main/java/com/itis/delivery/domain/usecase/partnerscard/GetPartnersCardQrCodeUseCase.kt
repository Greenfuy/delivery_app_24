package com.itis.delivery.domain.usecase.partnerscard

import android.graphics.Bitmap
import com.itis.delivery.data.exceptions.UserNotAuthorizedException
import com.itis.delivery.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import qrcode.QRCode
import javax.inject.Inject


class GetPartnersCardQrCodeUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke() : Pair<Long, Bitmap> {
        val userId = userRepository.getCurrentUserId()
            ?: throw UserNotAuthorizedException("User not authorized")

        val code = userId.hashCode().toLong() and Int.MAX_VALUE.toLong()


        return withContext(dispatcher) {
            Pair(
                code,
                QRCode
                    .ofSquares()
                    .build(code.toString())
                    .render()
                    .nativeImage() as Bitmap
            )
        }
    }
}