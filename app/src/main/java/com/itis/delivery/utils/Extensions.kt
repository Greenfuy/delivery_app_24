package com.itis.delivery.utils

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.itis.delivery.presentation.model.ProductUiModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

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