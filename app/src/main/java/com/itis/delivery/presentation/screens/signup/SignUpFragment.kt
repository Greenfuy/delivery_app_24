package com.itis.delivery.presentation.screens.signup

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.itis.delivery.R
import com.itis.delivery.base.Regexes
import com.itis.delivery.databinding.FragmentSignUpBinding
import com.itis.delivery.presentation.base.BaseFragment
import com.itis.delivery.utils.safeNavigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : BaseFragment(R.layout.fragment_sign_up) {

    private val viewModel: SignUpViewModel by viewModels()
    private val viewBinding: FragmentSignUpBinding by viewBinding(FragmentSignUpBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners() {
        viewBinding.run {
            initTextListeners()
            initButtonListeners()

            notRegisteredMtv.setOnClickListener {
                findNavController().safeNavigate(
                    R.id.signUpFragment,
                    R.id.action_signUpFragment_to_signInFragment
                )
            }
        }
    }

    private fun initButtonListeners() {
        with(viewBinding) {
            signUpBtn.setOnClickListener {
                viewModel.onSignUpClick(
                    usernameEt.text.toString(),
                    emailEt.text.toString(),
                    passwordEt.text.toString(),
                    confirmPasswordEt.text.toString()
                )
                observe()
            }
        }
    }

    private fun initTextListeners() {
        with(viewBinding) {
            usernameEt.addTextChangedListener {
                if (usernameEt.text.toString().isEmpty()
                    || !usernameEt.text.toString().matches(Regexes.nameRegex)) {
                    usernameTil.error = getString(R.string.prompt_username_requirements)
                } else {
                    usernameTil.error = null
                }
                changeSignUpButtonEnabled()
            }
            emailEt.addTextChangedListener {
                if (emailEt.text.toString().isEmpty()
                    || !emailEt.text.toString().matches(Regexes.emailRegex)) {
                    emailTil.error = getString(R.string.prompt_email_requirements)
                } else {
                    emailTil.error = null
                }
                changeSignUpButtonEnabled()
            }
            passwordEt.addTextChangedListener {
                if (passwordEt.text.toString().isEmpty()
                    || !passwordEt.text.toString().matches(Regexes.passwordRegex)) {
                    passwordTil.error = getString(R.string.prompt_password_requirements)
                } else {
                    passwordTil.error = null
                }
                changeSignUpButtonEnabled()
            }
            confirmPasswordEt.addTextChangedListener {
                if (confirmPasswordEt.text.toString() != passwordEt.text.toString()) {
                    confirmPasswordTil.error =
                        getString(R.string.prompt_confirm_password_requirements)
                } else {
                    confirmPasswordTil.error = null
                }
                changeSignUpButtonEnabled()
            }
        }
    }

    private fun changeSignUpButtonEnabled() {
        with(viewBinding) {
            signUpBtn.isEnabled = usernameEt.error == null
                    && emailEt.error == null
                    && passwordEt.error == null
                    && confirmPasswordEt.error == null
                    && !usernameEt.text.isNullOrEmpty()
                    && !emailEt.text.isNullOrEmpty()
                    && !passwordEt.text.isNullOrEmpty()
                    && !confirmPasswordEt.text.isNullOrEmpty()
        }
    }

    private fun observe() {
        with(viewModel) {
            error.observe {
                if (it != null) {
                    showAuthError(it)
                    viewBinding.passwordEt.text = null
                    viewBinding.confirmPasswordEt.text = null
                    viewBinding.signUpBtn.isEnabled = false
                }
            }
            success.observe {
                if (it) findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
            }
        }
    }
}