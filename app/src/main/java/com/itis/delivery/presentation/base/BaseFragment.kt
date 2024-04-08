package com.itis.delivery.presentation.base

import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.itis.delivery.R
import com.itis.delivery.utils.AuthErrors
import com.itis.delivery.utils.observe
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow


abstract class BaseFragment(@LayoutRes layout: Int) : Fragment(layout) {

    private fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(requireContext(), message, duration).show()
    }

    inline fun <T> Flow<T>.observe(crossinline block: (T) -> Unit): Job {
        return observe(fragmentLifecycleOwner = this@BaseFragment.viewLifecycleOwner, block)
    }

    protected fun showAuthError(error: AuthErrors) {
        when (error) {
            AuthErrors.WAIT -> showToast(getString(R.string.wait_error))
            AuthErrors.INVALID_DATA -> showToast(getString(R.string.invalid_data))
            AuthErrors.UNEXPECTED -> showToast(getString(R.string.unexpected_error))
            AuthErrors.INVALID_CREDENTIALS -> showToast(getString(R.string.invalid_credentials))
            AuthErrors.USER_ALREADY_EXISTS -> showToast(getString(R.string.user_already_exists))
        }
    }
}