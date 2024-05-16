package com.itis.delivery.presentation.screens.productpage

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.itis.delivery.R
import com.itis.delivery.data.exceptions.UserNotAuthorizedException
import com.itis.delivery.databinding.FragmentProductPageBinding
import com.itis.delivery.presentation.base.BaseFragment
import com.itis.delivery.utils.getShortDescription
import com.itis.delivery.utils.safeNavigate
import com.itis.delivery.utils.toPrice
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProductPageFragment : BaseFragment(R.layout.fragment_product_page) {

    private val viewBinding: FragmentProductPageBinding
    by viewBinding(FragmentProductPageBinding::bind)
    @Inject
    lateinit var viewModelFactory: ProductPageViewModel.Factory
    private val viewModel: ProductPageViewModel by viewModels {
        ProductPageViewModel.provideFactory(
            viewModelFactory,
            requireArguments().getLong(PRODUCT_ID)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.run {
            root.visibility = View.GONE

            observe()

            initListeners()
        }
    }

    private fun inCartCountObserve() {
        viewModel.inCartCount.observe {
            Log.d("ProductPageFragment", "inCart: $it")
            viewBinding.mtvInCartCount.text = it.toString()
            if (it == 0L) {
                viewBinding.btnMinus.isEnabled = false
                viewBinding.btnAddToCart.visibility = View.VISIBLE
                viewBinding.btnToCart.visibility = View.GONE
            } else {
                viewBinding.btnMinus.isEnabled = true
                viewBinding.btnAddToCart.visibility = View.GONE
                viewBinding.btnToCart.visibility = View.VISIBLE
            }
        }
    }

    private fun observe() {
        with(viewModel) {
            inCartCountObserve()

            product.observe {
                viewBinding.swipeRefresh.isRefreshing = false
                if (it != null) {
                    Log.d("ProductPageFragment", "productId: ${it.id}")
                    viewBinding.mtvProductName.text = it.name
                    viewBinding.mtvPrice.text = toPrice(it.price)
                    Glide.with(viewBinding.root)
                        .load(it.imageUrl)
                        .error(R.drawable.no_image)
                        .into(viewBinding.ivProduct)
                    changeErrorVisibility(
                        isVisible = false,
                        viewBinding = viewBinding,
                        btnOnClickListener = { viewModel.refresh() }
                    )
                    viewBinding.mtvDesc.text = getShortDescription(it)
                    viewBinding.root.visibility = View.VISIBLE
                }
            }

            rate.observe {
                if (it >= 0.0) {
                    viewBinding.mtvRating.text = it.toString()
                    viewBinding.rbRating.rating = it.toFloat()
                }
            }

            lifecycleScope.launch {
                errorsChannel.consumeEach { error ->
                    if (error is UserNotAuthorizedException) {
                        showSignInSnackBar()
                    } else {
                        Log.e("ProductPageFragment", "error: $error", error)
                        changeErrorVisibility(
                            isVisible = true,
                            viewBinding = viewBinding,
                            btnOnClickListener = { viewModel.refresh() }
                        )
                    }
                }
            }
        }
    }

    private fun showSignInSnackBar() {
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

    private fun initListeners() {
        with(viewBinding) {
            btnBuyNow.setOnClickListener {
                // TODO: navigate to order
            }

            btnAddToCart.setOnClickListener {
                val fl = viewModel.addToCart()
                if (fl) {
                    btnMinus.isEnabled = true
                }
                else showSignInSnackBar()
            }

            btnToCart.setOnClickListener {
                findNavController().safeNavigate(
                    R.id.productPageFragment,
                    R.id.action_productPageFragment_to_cartFragment
                )
            }

            btnPlus.setOnClickListener {
                val fl = viewModel.addToCart()
                if (fl) {
                    btnMinus.isEnabled = true
                }
                else showSignInSnackBar()
            }

            btnMinus.setOnClickListener {
                val fl = viewModel.removeFromCart()
                if (!fl) showSignInSnackBar()
            }

            swipeRefresh.setOnRefreshListener {
                viewModel.refresh()
            }
        }
    }

    companion object {
        const val PRODUCT_ID = "product_id"

        fun newInstance(productId: Long) =
            ProductPageFragment().apply {
                arguments = Bundle().apply {
                    putLong(PRODUCT_ID, productId)
                }
            }
    }
}