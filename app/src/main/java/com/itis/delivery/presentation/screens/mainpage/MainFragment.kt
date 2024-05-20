package com.itis.delivery.presentation.screens.mainpage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.itis.delivery.R
import com.itis.delivery.base.Keys.CATEGORY_TAG
import com.itis.delivery.base.Keys.PRODUCT_ID
import com.itis.delivery.base.Keys.SEARCH_TERM
import com.itis.delivery.databinding.FragmentMainBinding
import com.itis.delivery.presentation.adapter.MainAdapter
import com.itis.delivery.presentation.adapter.MainAdapter.Companion.KEY_CATEGORY
import com.itis.delivery.presentation.adapter.MainAdapter.Companion.KEY_SEARCH
import com.itis.delivery.presentation.base.BaseFragment
import com.itis.delivery.presentation.model.CategoryUiModel
import com.itis.delivery.presentation.model.ProductUiModel
import com.itis.delivery.utils.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : BaseFragment(R.layout.fragment_main) {

    private val viewModel: MainViewModel by viewModels()
    private val viewBinding: FragmentMainBinding by viewBinding(FragmentMainBinding::bind)

    private var adapter: MainAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.run {
            observe()

            rvMain.layoutManager = GridLayoutManager(requireContext(), 2)
                .apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int) =
                        if (
                            rvMain.adapter?.getItemViewType(position) == KEY_CATEGORY
                            || rvMain.adapter?.getItemViewType(position) == KEY_SEARCH
                            ) 2 else 1
                    }
                }
            rvMain.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView.canScrollVertically(1)) {
                        viewModel.getProductList()
                    }
                }
            })

            swipeRefresh.setOnRefreshListener {
                observe()

                viewModel.refresh()
            }
        }
    }

    override fun onPause() {
        super.onPause()

        changeLoadingVisibility(root = viewBinding.root,  isVisible = false)
        changeErrorVisibility(
            root = viewBinding.root,
            isVisible = false,
            btnOnClickListener = {}
        )
    }


    override fun onResume() {
        super.onResume()
        viewModel.refresh()
        viewBinding.rvMain.adapter = adapter
    }

    private fun onProductClicked(product: ProductUiModel) {
        findNavController().safeNavigate(
            R.id.mainFragment,
            R.id.action_mainFragment_to_productPageFragment,
            Bundle().apply { putLong(PRODUCT_ID, product.id) }
        )
    }

    private fun onSearchClicked(searchTerm: String) {
        findNavController().safeNavigate(
            R.id.mainFragment,
            R.id.action_mainFragment_to_resultSearchFragment,
            Bundle().apply { putString(SEARCH_TERM, searchTerm) }
        )
    }

    private fun onCategoryClicked(category: CategoryUiModel) {
        findNavController().safeNavigate(
            R.id.mainFragment,
            R.id.action_mainFragment_to_resultSearchFragment,
            Bundle().apply { putString(CATEGORY_TAG, category.name) }
        )
    }

    private fun observe() {
        with(viewModel) {
            productList.observe {
                viewBinding.swipeRefresh.isRefreshing = false
                if (it.isNotEmpty()) {
                    if (adapter == null) {
                        adapter = MainAdapter(
                            productList = it.toMutableList(),
                            onSearchClick = ::onSearchClicked,
                            onCategoryClick = ::onCategoryClicked,
                            onProductClick = ::onProductClicked,
                            categoryList = categoryList
                        )
                        viewBinding.rvMain.adapter = adapter
                    } else {
                        adapter?.addProducts(it)
                    }
                }
            }

            isLoading.observe {
                changeErrorVisibility(
                    root = viewBinding.root,
                    isVisible = false,
                    btnOnClickListener = {}
                )
                changeLoadingVisibility(root = viewBinding.root, isVisible = it)
            }

            lifecycleScope.launch {
                errorsChannel.consumeEach {
                    changeErrorVisibility(
                        root = viewBinding.root,
                        isVisible = true,
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