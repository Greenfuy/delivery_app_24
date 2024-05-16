package com.itis.delivery.presentation.screens.mainpage

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.itis.delivery.R
import com.itis.delivery.databinding.FragmentMainBinding
import com.itis.delivery.presentation.adapter.MainAdapter
import com.itis.delivery.presentation.base.BaseFragment
import com.itis.delivery.presentation.model.ProductUiModel
import com.itis.delivery.presentation.screens.productpage.ProductPageFragment.Companion.PRODUCT_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : BaseFragment(R.layout.fragment_main) {

    private val viewModel: MainViewModel by viewModels()
    private val viewBinding: FragmentMainBinding by viewBinding(FragmentMainBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.run {
            observe()

            rvMain.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )

            swipeRefresh.setOnRefreshListener {
                viewModel.refresh()

            }
        }
    }

    private fun onProductClicked(product: ProductUiModel) {
        findNavController().navigate(
            R.id.action_mainFragment_to_productPageFragment,
            Bundle().apply { putLong(PRODUCT_ID, product.id) }
            )
    }

    private fun observe() {
        with(viewModel) {
            productList.observe {
                viewBinding.swipeRefresh.isRefreshing = false
                if (it.isNotEmpty()) {
                    viewBinding.rvMain.adapter = MainAdapter(
                        productList = it,
                        onSearchClick = {},
                        onCategoryClick = {},
                        onProductClick = ::onProductClicked,
                        categoryList = categoryList
                    )
                    changeErrorVisibility(
                        isVisible = false,
                        viewBinding = viewBinding,
                        btnOnClickListener = {viewModel.refresh()}
                    )
                }
            }

            isLoading.observe {
                // TODO: show progress
            }

            lifecycleScope.launch {
                errorsChannel.consumeEach { error ->
                    val errorMessage = error.message ?: getString(R.string.unknown_error)
                    Log.e("MainFragment", "Error: $errorMessage", error)
                    changeErrorVisibility(
                        isVisible = true,
                        viewBinding = viewBinding,
                        btnOnClickListener = {viewModel.refresh()}
                    )
                }
            }

        }
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}