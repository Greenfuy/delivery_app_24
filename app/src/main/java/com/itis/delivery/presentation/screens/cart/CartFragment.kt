package com.itis.delivery.presentation.screens.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.itis.delivery.R
import com.itis.delivery.base.Keys
import com.itis.delivery.base.Keys.PRODUCT_ID
import com.itis.delivery.databinding.FragmentCartBinding
import com.itis.delivery.presentation.adapter.CartAdapter
import com.itis.delivery.presentation.base.BaseFragment
import com.itis.delivery.presentation.model.CartProductModel
import com.itis.delivery.utils.safeNavigate
import com.itis.delivery.utils.toPrice
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : BaseFragment(R.layout.fragment_cart) {

    private var _viewBinding: FragmentCartBinding? = null
    private val viewBinding get() = _viewBinding!!

    private val viewModel: CartViewModel by viewModels()

    private var cartProducts = mutableListOf<CartProductModel>()
    private var adapter: CartAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentCartBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewBinding) {
            rvCart.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            btnDelete.setOnClickListener {
                viewModel.removeAllFromCart(
                    productIds = cartProducts
                        .filter{ it.isChosen }
                        .map { it.productId }
                        .toLongArray()
                )
            }

            cbChooseAll.setOnClickListener {
                viewModel.getCartProductList()

                cartProducts.forEach { it.isChosen = cbChooseAll.isChecked }
            }


            swipeRefresh.setOnRefreshListener {
                viewModel.refresh()
            }

            btnOrder.setOnClickListener {
                findNavController().safeNavigate(
                    R.id.cartFragment,
                    R.id.action_cartFragment_to_orderFragment,
                    Bundle().apply {
                        putLongArray(
                            Keys.PRODUCTS,
                            cartProducts
                                .filter { it.isChosen }
                                .map { it.productId }
                                .toLongArray()
                        )
                    }
                )
            }
        }

        observe()
    }

    override fun onPause() {
        super.onPause()

        setLoadingVisibility(root = viewBinding.swipeRefresh,  isVisible = false)
        setErrorVisibility(
            root = viewBinding.swipeRefresh,
            isVisible = false,
            btnOnClickListener = {}
        )
    }

    override fun onResume() {
        super.onResume()

        viewModel.refresh()
    }

    private fun onItemCartProductClicked(cartProductModel: CartProductModel) {
        findNavController().safeNavigate(
            R.id.cartFragment,
            R.id.action_cartFragment_to_productPageFragment,
            Bundle().apply { putLong(PRODUCT_ID, cartProductModel.productId) }
        )
    }

    private fun onChosenChecked(productId: Long, isChecked: Boolean) {
        with(viewBinding) {
            cartProducts.find { it.productId == productId }?.isChosen = isChecked
            btnDelete.isEnabled = cartProducts.any { it.isChosen }
            btnOrder.isEnabled = cartProducts.any { it.isChosen }
            cbChooseAll.isChecked = cartProducts.all { it.isChosen }

            viewModel.getCartProductList()
        }
    }

//    private fun onIncreaseCountClicked(productId: Long) {
//        viewModel.addToCart(productId)
//    }
//
//    private fun onDecreaseCountClicked(productId: Long) {
//        viewModel.removeFromCart(productId)
//    }

    private fun observe() {
        with(viewModel) {
            cartProductList.observe {
                viewBinding.swipeRefresh.isRefreshing = false
                if (it.isNotEmpty()) {
                    setEmptyCartTextVisibility(false)

                    setChooses(it)
                    calculatePriceAndCount()

                    if (adapter == null) {
                        adapter = CartAdapter(
                            onItemCartProductClick = ::onItemCartProductClicked,
                            onChosenCheck = ::onChosenChecked,
                            onIncreaseCountClick = { /*onIncreaseCountClicked(it)*/ },
                            onDecreaseCountClick = { /*onDecreaseCountClicked(it)*/ }
                        ).apply { setList(cartProducts) }
                    }

                    viewBinding.rvCart.adapter = adapter?.apply { setList(cartProducts) }

                } else {
                    setEmptyCartTextVisibility(true)
                }
            }
            isLoading.observe {
                setErrorVisibility(
                    root = viewBinding.root,
                    isVisible = false,
                    btnOnClickListener = {}
                )
                setLoadingVisibility(
                    root = viewBinding.root,
                    isVisible = it
                )
            }
            isRemoved.observe {
                if (it) {
                    showSnackBar(getString(R.string.prompt_products_removed))
                }
            }
            lifecycleScope.launch {
                errorsChannel.consumeEach {
                    setErrorVisibility(
                        root = viewBinding.root,
                        isVisible = true,
                        btnOnClickListener = { viewModel.refresh() }
                    )
                }
            }
        }
    }

    private fun setChooses(list: List<CartProductModel>) {
        viewBinding.cbChooseAll.isChecked = cartProducts.all { it.isChosen }
        if (cartProducts.isNotEmpty()) {
            list.forEach { i ->
                cartProducts.find { j -> i.productId == j.productId }?.let { j ->
                    i.isChosen = j.isChosen
                }
            }
        }
        cartProducts = list.toMutableList()
    }

    private fun calculatePriceAndCount() {
        viewBinding.mtvCount.text = getString(
            R.string.prompt_items,
            cartProducts.filter { it.isChosen }
                .sumOf { it.count }
                .toString()
        )
        viewBinding.mtvTotal.text = toPrice(
            cartProducts.filter { it.isChosen }
                .sumOf { it.price * it.count }
                .toInt()
        )
    }

    private fun setEmptyCartTextVisibility(isVisible: Boolean) {
        with(viewBinding) {
            tbTitle.visibility = if (isVisible) View.GONE else View.VISIBLE
            rvCart.visibility = if (isVisible) View.GONE else View.VISIBLE
            cardOrder.visibility = if (isVisible) View.GONE else View.VISIBLE
            mtvEmptyResult.visibility = if (isVisible) View.VISIBLE else View.GONE
        }
    }
}