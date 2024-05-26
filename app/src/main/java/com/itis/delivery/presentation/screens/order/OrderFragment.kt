package com.itis.delivery.presentation.screens.order

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.itis.delivery.R
import com.itis.delivery.base.Keys
import com.itis.delivery.data.exceptions.UserNotAuthorizedException
import com.itis.delivery.databinding.FragmentOrderBinding
import com.itis.delivery.presentation.adapter.CartOrderAdapter
import com.itis.delivery.presentation.base.BaseFragment
import com.itis.delivery.utils.safeNavigate
import com.itis.delivery.utils.toPrice
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class OrderFragment : BaseFragment(R.layout.fragment_order) {

    @Inject
    lateinit var viewModelFactory: OrderViewModel.Factory
    private val viewModel: OrderViewModel by viewModels {
        OrderViewModel.provideFactory(
            viewModelFactory,
            requireArguments().getLongArray(Keys.PRODUCTS)
        )
    }
    private val viewBinding: FragmentOrderBinding by viewBinding(FragmentOrderBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.rvProducts.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        observe()

        initButtonListeners()
        initTextListeners()
    }

    override fun onPause() {
        super.onPause()

        setErrorVisibility(
            viewBinding.root,
            isVisible = false,
            btnOnClickListener = {}
        )
        setLoadingVisibility(
            viewBinding.root,
            isVisible = false
        )
    }

    override fun onResume() {
        super.onResume()

        viewModel.getCartOrderList()
    }

    private fun initButtonListeners() {
        with(viewBinding) {
            btnChangeAddress.setOnClickListener {
                layoutAddress.visibility = View.GONE
                layoutChangeAddress.visibility = View.VISIBLE
            }

            btnSubmit.setOnClickListener {
                mtvAddress.text =
                    getString(
                        R.string.prompt_form_address,
                        etApartment.text.toString(),
                        etStreet.text.toString(),
                        etCity.text.toString(),
                        etRegion.text.toString()
                    )
                layoutAddress.visibility = View.VISIBLE
                layoutChangeAddress.visibility = View.GONE
                btnOrder.isEnabled = true
            }

            btnOrder.setOnClickListener {
                AlertDialog.Builder(requireContext())
                    .setMessage(getString(R.string.prompt_order_confirm))
                    .setPositiveButton("Yes") { _, _ ->
                        viewModel.createOrder(mtvAddress.text.toString())
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
        }
    }

    private fun initTextListeners() {
        with(viewBinding) {
            etApartment.addTextChangedListener {
                if (it.toString().isEmpty() || it.toString().length > 4) {
                    etApartment.error = getString(R.string.invalid_data)
                    btnSubmit.isEnabled = false
                } else {
                    etApartment.error = null
                }
                changeBtnSubmitEnabled()
            }

            etStreet.addTextChangedListener {
                if (it.toString().isEmpty()) {
                    etStreet.error = getString(R.string.prompt_error_empty_field)
                    btnSubmit.isEnabled = false
                } else {
                    etStreet.error = null
                }
                changeBtnSubmitEnabled()
            }

            etCity.addTextChangedListener {
                if (it.toString().isEmpty()) {
                    etCity.error = getString(R.string.prompt_error_empty_field)
                } else {
                    etCity.error = null
                }
                changeBtnSubmitEnabled()
            }

            etRegion.addTextChangedListener {
                if (it.toString().isEmpty()) {
                    etRegion.error = getString(R.string.prompt_error_empty_field)
                    btnSubmit.isEnabled = false
                } else {
                    etRegion.error = null
                }
                changeBtnSubmitEnabled()
            }
        }
    }

    private fun changeBtnSubmitEnabled() {
        with(viewBinding) {
            btnSubmit.isEnabled = etApartment.error == null
                    && etStreet.error == null
                    && etCity.error == null
                    && etRegion.error == null
                    && !etApartment.text.isNullOrEmpty()
                    && !etStreet.text.isNullOrEmpty()
                    && !etCity.text.isNullOrEmpty()
                    && !etRegion.text.isNullOrEmpty()
        }
    }

    private fun observe() {
        with(viewModel) {
            cartOrderList.observe {
                if (it.isNotEmpty()) {
                    viewBinding.rvProducts.adapter = CartOrderAdapter()
                        .apply { setList(it) }

                    viewBinding.mtvCount.text =
                        getString(
                            R.string.prompt_items,
                            it.sumOf { c -> c.count }.toString()
                        )
                    viewBinding.mtvPrice.text = toPrice(it.sumOf { c -> c.price * c.count }.toInt())
                }
            }
            isLoading.observe {
                setErrorVisibility(
                    root = viewBinding.root,
                    isVisible = false,
                    btnOnClickListener = {}
                )
                setLoadingVisibility(root = viewBinding.root, isVisible = it)
            }
            success.observe {
                if (it) {
                    showSnackBar(getString(R.string.prompt_order_success))
                    findNavController().safeNavigate(
                        R.id.orderFragment,
                        R.id.action_orderFragment_to_mainFragment
                    )
                }
            }
            lifecycleScope.launch {
                errorsChannel.consumeEach {
                    if (it is UserNotAuthorizedException)
                        showSnackBar(getString(R.string.prompt_session_time_out_error))
                    else
                        showSnackBar(getString(R.string.prompt_some_error))
                    Log.e("OrderFragment", "Error: $it", it)
                    findNavController().safeNavigate(
                        R.id.orderFragment,
                        R.id.action_orderFragment_to_mainFragment
                    )
                }
            }
        }
    }
}