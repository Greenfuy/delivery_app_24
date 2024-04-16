package com.itis.delivery.presentation.screens.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.itis.delivery.R
import com.itis.delivery.databinding.FragmentMainBinding
import com.itis.delivery.presentation.adapter.MainAdapter
import com.itis.delivery.presentation.base.BaseFragment
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
        }
    }

    private fun observe() {
        with(viewModel) {
            productList.observe {
                if (it.isNotEmpty()) {
                    viewBinding.rvMain.adapter = MainAdapter(
                        productList = it,
                        onSearchClick = {},
                        onCategoryClick = {},
                        onProductClick = {},
                        onProductToCartClick = {},
                        categoryList = categoryList
                    )
                    changeErrorVisibility(isVisible = false)
                }
            }

            isLoading.observe {
                // TODO: show progress
            }

            lifecycleScope.launch {
                errorsChannel.consumeEach { error ->
                    val errorMessage = error.message ?: getString(R.string.unknown_error)
                    Log.e("MainFragment", "Error: $errorMessage", error)
                    changeErrorVisibility(isVisible = true)
                }
            }

        }
    }

    private fun changeErrorVisibility(isVisible: Boolean) {
        with(viewBinding) {
            mtvError.visibility = if (isVisible) View.VISIBLE else View.GONE
            rvMain.visibility = if (isVisible) View.GONE else View.VISIBLE
        }
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}