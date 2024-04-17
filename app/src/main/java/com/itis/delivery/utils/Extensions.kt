package com.itis.delivery.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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