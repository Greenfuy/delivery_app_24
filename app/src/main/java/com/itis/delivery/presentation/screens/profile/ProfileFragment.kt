package com.itis.delivery.presentation.screens.profile

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.itis.delivery.R
import com.itis.delivery.base.Regexes
import com.itis.delivery.data.exceptions.UserNotAuthorizedException
import com.itis.delivery.databinding.FragmentProfileBinding
import com.itis.delivery.presentation.activitymain.MainActivity.Companion.DELIVERY_PREFS
import com.itis.delivery.presentation.activitymain.MainActivity.Companion.THEME_MODE
import com.itis.delivery.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    private val viewModel: ProfileViewModel by viewModels()
    private val viewBinding: FragmentProfileBinding by viewBinding(FragmentProfileBinding::bind)

    private var oldUsername: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe()

        viewBinding.run {
            initThemeChangeRadioGroup()

            etName.addTextChangedListener {
                if (etName.text?.matches(Regexes.nameRegex) == false) {
                    etName.error = getString(R.string.prompt_username_requirements)
                } else if (etName.text?.toString() == oldUsername) {
                    etName.error = getString(R.string.prompt_error_same_username)
                } else {
                    etName.error = null
                }
                btnSubmit.isEnabled = etName.error.isNullOrEmpty()
            }

            btnSubmit.setOnClickListener {
                viewModel.changeUserCredentials(etName.text.toString())
            }

            btnReturn.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun initThemeChangeRadioGroup() {
        with(viewBinding) {
            val theme = AppCompatDelegate.getDefaultNightMode()

            when (theme) {
                AppCompatDelegate.MODE_NIGHT_NO -> rbLight.isChecked = true
                AppCompatDelegate.MODE_NIGHT_YES -> rbDark.isChecked = true
                else -> rbDaynight.isChecked = true
            }

            rgTheme.setOnCheckedChangeListener { _, checkedId ->
                AppCompatDelegate.setDefaultNightMode(
                    when (checkedId) {
                        R.id.rb_light -> AppCompatDelegate.MODE_NIGHT_NO
                        R.id.rb_dark -> AppCompatDelegate.MODE_NIGHT_YES
                        else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    }
                )

                requireActivity()
                    .getSharedPreferences(DELIVERY_PREFS, Context.MODE_PRIVATE)
                    .edit()
                    .putInt(THEME_MODE, AppCompatDelegate.getDefaultNightMode())
                    .apply()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUserCredentials()
    }

    private fun observe() {
        with(viewModel) {
            credentials.observe {
                if (!it.isNullOrEmpty()) {
                    oldUsername = it
                }
            }
            isCredentialsUpdated.observe {
                viewBinding.btnSubmit.isEnabled = false
                if (it) showSnackBar(getString(R.string.prompt_successful_username_change))
            }
            lifecycleScope.launch {
                errorsChannel.consumeEach {
                    if (it is UserNotAuthorizedException) {
                        showSignInSnackBar()
                        findNavController().popBackStack()
                    } else {
                        setErrorVisibility(
                            root = viewBinding.root,
                            isVisible = true
                        )
                    }
                }
            }
        }
    }
}