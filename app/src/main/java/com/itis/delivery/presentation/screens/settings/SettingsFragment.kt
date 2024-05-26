package com.itis.delivery.presentation.screens.settings

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.itis.delivery.R
import com.itis.delivery.data.exceptions.UserNotAuthorizedException
import com.itis.delivery.databinding.FragmentSettingsBinding
import com.itis.delivery.presentation.base.BaseFragment
import com.itis.delivery.presentation.screens.settings.AboutBottomSheetFragment.Companion.ABOUT_BOTTOM_SHEET_FRAGMENT_TAG
import com.itis.delivery.utils.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    private val viewModel: SettingsViewModel by viewModels()
    private val viewBinding: FragmentSettingsBinding by viewBinding(FragmentSettingsBinding::bind)

    private var isUserAuthorized = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe()

        viewBinding.run {

        }
    }

    override fun onPause() {
        super.onPause()

        setErrorVisibility(
            root = viewBinding.root,
            isVisible = false,
            btnOnClickListener = {}
        )
        setLoadingVisibility(
            root = viewBinding.root,
            isVisible = false
        )
    }

    override fun onResume() {
        super.onResume()

        viewModel.getUserCredentials()
        viewModel.getQrCode()
    }

    private fun initListeners() {
        with(viewBinding) {
            viewBinding.cardSignOut.visibility = if (isUserAuthorized) View.VISIBLE else View.GONE
            viewBinding.cardPartnerCard.visibility =
                if (isUserAuthorized) View.VISIBLE else View.GONE

            cardProfile.setOnClickListener {
                if (isUserAuthorized) {
                    findNavController().safeNavigate(
                        R.id.settingsFragment,
                        R.id.action_settingsFragment_to_profileFragment
                    )
                } else {
                    findNavController().safeNavigate(
                        R.id.settingsFragment,
                        R.id.action_settingsFragment_to_signInFragment
                    )
                }
            }

            cardPartnerCard.setOnClickListener {
                if (isUserAuthorized) {

                }
            }
            cardOrders.setOnClickListener {
                if (isUserAuthorized) {
                    findNavController().safeNavigate(
                        R.id.settingsFragment,
                        R.id.action_settingsFragment_to_orderHistoryFragment
                    )
                } else {
                    showSignInSnackBar()
                }
            }
            cardAbout.setOnClickListener {
                AboutBottomSheetFragment
                    .newInstance()
                    .show(parentFragmentManager, ABOUT_BOTTOM_SHEET_FRAGMENT_TAG)
            }
            cardSignOut.setOnClickListener {
                if (isUserAuthorized) {
                    AlertDialog.Builder(requireContext())
                        .setMessage(getString(R.string.prompt_sign_out_confirm))
                        .setPositiveButton(getString(R.string.yes)) { _, _ -> viewModel.signOut() }
                        .setNegativeButton(getString(R.string.no)) { dialog, _ -> dialog.dismiss() }
                        .show()
                } else {
                    showSignInSnackBar()
                }
            }
        }
    }

    private fun observe() {
        with(viewModel) {
            userCredentials.observe {
                if (it != null) {
                    isUserAuthorized = true
                    viewBinding.mtvProfile.text = it
                } else {
                    isUserAuthorized = false
                }
                initListeners()
            }
            qrCode.observe {
                if (it != null) {
                    viewBinding.mtvPartnersCardCode.text = it.first.toString()
                    viewBinding.ivQr.setImageBitmap(it.second)
                }
            }
            isLoading.observe {
                setLoadingVisibility(
                    root = viewBinding.root,
                    isVisible = it
                )
                setErrorVisibility(
                    root = viewBinding.root,
                    isVisible = false,
                    btnOnClickListener = {
                        viewModel.getUserCredentials()
                        viewModel.getQrCode()
                    }
                )
            }
            lifecycleScope.launch {
                errorsChannel.consumeEach {
                    if (it is UserNotAuthorizedException) {
                        isUserAuthorized = false
                    } else {
                        setErrorVisibility(
                            root = viewBinding.root,
                            isVisible = true,
                            btnOnClickListener = {
                                viewModel.getUserCredentials()
                                viewModel.getQrCode()
                            }
                        )
                    }
                }
            }
        }
    }
}