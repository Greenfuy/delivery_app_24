package com.itis.delivery.presentation.base

import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.itis.delivery.R
import com.itis.delivery.base.AuthErrors
import com.itis.delivery.utils.observe
import com.itis.delivery.utils.safeNavigate
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow


abstract class BaseFragment(@LayoutRes layout: Int) : Fragment(layout) {

    protected fun showSnackBar(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
        Snackbar.make(
            requireView(),
            message,
            duration
        ).show()
    }

    inline fun <T> Flow<T>.observe(crossinline block: (T) -> Unit): Job {
        return observe(fragmentLifecycleOwner = this@BaseFragment.viewLifecycleOwner, block)
    }

    protected fun showAuthError(error: AuthErrors) {
        when (error) {
            AuthErrors.WAIT -> showSnackBar(getString(R.string.wait_error))
            AuthErrors.INVALID_DATA -> showSnackBar(getString(R.string.invalid_data))
            AuthErrors.UNEXPECTED -> showSnackBar(getString(R.string.unexpected_error))
            AuthErrors.INVALID_CREDENTIALS -> showSnackBar(getString(R.string.invalid_credentials))
            AuthErrors.USER_ALREADY_EXISTS -> showSnackBar(getString(R.string.user_already_exists))
        }
    }

    protected fun setErrorVisibility(
        root: View,
        isVisible: Boolean,
        btnOnClickListener: View.OnClickListener = View.OnClickListener {}
    ) {
        val errorLayout = activity?.findViewById<View>(R.id.layout_error)
        errorLayout?.visibility = if (isVisible) View.VISIBLE else View.GONE

        val tryAgainBtn = activity?.findViewById<View>(R.id.btn_try_again)
        tryAgainBtn?.setOnClickListener(btnOnClickListener)
        root.visibility = if (isVisible) View.GONE else View.VISIBLE
    }

    protected fun setLoadingVisibility(root: View, isVisible: Boolean) {
        val loadingLayout = activity?.findViewById<View>(R.id.layout_loading)
        loadingLayout?.visibility = if (isVisible) View.VISIBLE else View.GONE

        root.visibility = if (isVisible) View.GONE else View.VISIBLE
    }

    protected fun showSignInSnackBar() {
        Snackbar.make(requireView(),
            getString(R.string.prompt_must_be_authorized), Snackbar.LENGTH_SHORT)
            .setAction(getString(R.string.action_sign_in)) {
                findNavController().safeNavigate(
                    R.id.productPageFragment,
                    R.id.action_productPageFragment_to_signInFragment
                )
            }
            .show()
    }
}