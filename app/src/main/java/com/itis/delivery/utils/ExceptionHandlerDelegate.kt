package com.itis.delivery.utils

import com.itis.delivery.data.exceptions.TooManyRequestsException
import retrofit2.HttpException
import javax.inject.Inject

class ExceptionHandlerDelegate @Inject constructor() {

    fun handleException(ex: Throwable): Throwable {
        return when (ex) {
            is HttpException -> {
                when (ex.code()) {
                    401 -> {
                        ex
                    }

                    403 -> {
                        ex
                    }

                    429 -> {
                        TooManyRequestsException(message = "Too many requests")
                    }

                    else -> {
                        ex
                    }
                }
            }

            else -> {
                ex
            }
        }
    }
}