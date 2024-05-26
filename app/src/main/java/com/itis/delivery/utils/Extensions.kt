package com.itis.delivery.utils

import android.content.Context
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.itis.delivery.R
import com.itis.delivery.domain.model.OrderDomainModel.Companion.CREATED
import com.itis.delivery.domain.model.OrderDomainModel.Companion.DELIVERED
import com.itis.delivery.domain.model.OrderDomainModel.Companion.IN_PROGRESS
import com.itis.delivery.presentation.model.ProductUiModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

inline fun <T> Flow<T>.observe(
    fragmentLifecycleOwner: LifecycleOwner,
    crossinline block: (T) -> Unit
): Job {
    return fragmentLifecycleOwner.lifecycleScope.launch {
        fragmentLifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            collect { data -> block(data) }
        }
    }
}

inline fun <R> runSuspendCatching(
    exceptionHandlerDelegate: ExceptionHandlerDelegate,
    block: () -> R
): Result<R> {
    return try {
        Result.success(block())
    } catch (c: CancellationException) {
        throw c
    } catch (e: Throwable) {
        Result.failure(exceptionHandlerDelegate.handleException(e))
    }
}


fun NavController.safeNavigate(direction: NavDirections) {
    currentDestination?.getAction(direction.actionId)?.run { navigate(direction) }
}
fun NavController.safeNavigate(
    @IdRes currentDestinationId: Int,
    @IdRes id: Int,
    args: Bundle? = null
) {
    if (currentDestinationId == currentDestination?.id) {
        navigate(id, args)
    }
}


fun toPrice(price: Int) = "$price₽"

fun getShortDescription(product: ProductUiModel) =
    "Product code: ${product.id} \nBrand: ${product.brands}"

fun getTextByOrderState(state: Int, context: Context) =
    when (state) {
        CREATED -> context.getString(R.string.prompt_order_created)
        IN_PROGRESS -> context.getString(R.string.prompt_order_in_progress)
        DELIVERED -> context.getString(R.string.prompt_order_delivered)
        else -> context.getString(R.string.prompt_order_cancelled)
    }

fun convertTimestampToDateString(timestamp: Long): String {
    val localDateTime = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(timestamp),
        ZoneId.systemDefault()
    )
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yy")
    return localDateTime.format(formatter)
}

fun convertTimestampToDateTimeString(timestamp: Long): String {
    val localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
    val formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yy")
    return localDateTime.format(formatter)
}