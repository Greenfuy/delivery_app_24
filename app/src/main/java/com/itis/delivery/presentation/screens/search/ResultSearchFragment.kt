package com.itis.delivery.presentation.screens.search

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.itis.delivery.R
import com.itis.delivery.base.Keys
import com.itis.delivery.base.Keys.CATEGORY_TAG
import com.itis.delivery.base.Keys.PRICE_END
import com.itis.delivery.base.Keys.PRICE_START
import com.itis.delivery.base.Keys.RATE
import com.itis.delivery.base.Keys.SEARCH_TERM
import com.itis.delivery.databinding.FragmentResultSearchBinding
import com.itis.delivery.presentation.adapter.ProductAdapter
import com.itis.delivery.presentation.base.BaseFragment
import com.itis.delivery.presentation.model.ProductUiModel
import com.itis.delivery.presentation.screens.productpage.ProductPageFragment
import com.itis.delivery.utils.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ResultSearchFragment : BaseFragment(R.layout.fragment_result_search) {

    private val viewModel: ResultSearchViewModel by viewModels()
    private val viewBinding: FragmentResultSearchBinding
    by viewBinding(FragmentResultSearchBinding::bind)

    private var categoryTag: String? = null
    private var searchTerm: String? = null
    private var rate: Int? = null
    private var priceStart: Int? = null
    private var priceEnd: Int? = null

    private var adapter: ProductAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryTag = arguments?.getString(CATEGORY_TAG)
        searchTerm = arguments?.getString(SEARCH_TERM)
        rate = arguments?.getInt(RATE)
        priceStart = arguments?.getInt(PRICE_START)
        priceEnd = arguments?.getInt(PRICE_END)
        Log.d("ResultSearchFragment",
            "categoryTag: $categoryTag, searchTerm: $searchTerm, rate: $rate, priceStart: $priceStart, priceEnd: $priceEnd")

        viewBinding.run {
            rvResult.layoutManager = StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
            )

            observe()
            getProductList()

            btnReturn.setOnClickListener {
                findNavController().popBackStack()
            }
            swipeRefresh.setOnRefreshListener { viewModel.refresh() }
            rvResult.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView.canScrollVertically(1)) {
                        getProductList()
                    }
                }
            })
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
    }

    private fun onProductClicked(product: ProductUiModel) {
        findNavController().safeNavigate(
            R.id.resultSearchFragment,
            R.id.action_resultSearchFragment_to_productPageFragment,
            Bundle().apply { putLong(Keys.PRODUCT_ID, product.id) }
        )
    }

    private fun getProductList() {
        if (categoryTag != null) {
            viewModel.getProductsByCategory(categoryTag!!)
            viewBinding.mtvTitle.text = categoryTag
        } else if (searchTerm != null) {
            viewModel.getProductsByPriceAndRate(
                searchTerm = searchTerm!!,
                rate = rate?: 0,
                priceStart = priceStart ?: 0,
                priceEnd = priceEnd ?: 0
            )
        }
    }

    private fun observe() {
        with(viewModel) {
            productList.observe {
                viewBinding.swipeRefresh.isRefreshing = false
                if (it.isNotEmpty()) {
                    if (adapter == null) {
                        adapter = ProductAdapter(
                            productList = it.toMutableList(),
                            onProductClick = ::onProductClicked
                        )
                        viewBinding.rvResult.adapter = adapter
                    } else {
                        adapter?.addProducts(it)
                    }
                    viewBinding.mtvEmptyResult.visibility = View.GONE
                    viewBinding.rvResult.visibility = View.VISIBLE
                } else {
                    if (adapter == null) {
                        viewBinding.mtvEmptyResult.visibility = View.VISIBLE
                        viewBinding.rvResult.visibility = View.GONE
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
                        btnOnClickListener = { viewModel.refresh() }
                    )
                }
            }
        }
    }

    companion object {
        fun newInstance(categoryTag: String) =
            ResultSearchFragment().apply {
                arguments = Bundle().apply {
                    putString(CATEGORY_TAG, categoryTag)
                }
            }


        fun newInstance(searchTerm: String, priceStart: Int, priceEnd: Int, rate: Int) =
            ProductPageFragment().apply {
                arguments = Bundle().apply {
                    putString(SEARCH_TERM, searchTerm)
                    putInt(PRICE_START, priceStart)
                    putInt(PRICE_END, priceEnd)
                    putInt(RATE, rate)
                }
            }
    }
}