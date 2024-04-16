package com.itis.delivery.presentation.screens.signin

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.itis.delivery.R
import com.itis.delivery.databinding.FragmentSignInBinding
import com.itis.delivery.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : BaseFragment(R.layout.fragment_sign_in) {

    private val viewModel: SignInViewModel by viewModels()
    private val viewBinding: FragmentSignInBinding by viewBinding(FragmentSignInBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        observe()
    }

    private fun initListeners() {
        viewBinding.run {
            initTextListeners()
            initButtonListeners()

            notRegisteredMtv.setOnClickListener {
                findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
            }
        }
    }

    private fun initButtonListeners() {
        with(viewBinding) {
            signInBtn.setOnClickListener {
                viewModel.onSignUpClick(
                    emailEt.text.toString(),
                    passwordEt.text.toString()
                )
                observe()
            }
        }
    }

    private fun initTextListeners() {
        with(viewBinding) {
            emailEt.addTextChangedListener {
                signInBtn.isEnabled = emailEt.error == null
                        && passwordEt.error == null
            }
            passwordEt.addTextChangedListener {
                signInBtn.isEnabled = emailEt.error == null
                        && passwordEt.error == null
            }
        }
    }

    private fun observe() {
        with(viewModel) {
            error.observe {
                if (it != null) {
                    showAuthError(it)
                    viewBinding.passwordEt.text = null
                    viewBinding.signInBtn.isEnabled = false
                }
            }

            success.observe {
                if (it) {
                    findNavController().navigate(R.id.action_signInFragment_to_mainFragment)
                }
            }
        }
    }
}