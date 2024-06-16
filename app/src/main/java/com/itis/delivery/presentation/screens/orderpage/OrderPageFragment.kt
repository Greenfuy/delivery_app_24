package com.itis.delivery.presentation.screens.orderpage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.itis.delivery.R
import com.itis.delivery.base.Keys
import com.itis.delivery.databinding.FragmentOrderPageBinding
import com.itis.delivery.domain.model.OrderDomainModel.Companion.DELIVERED
import com.itis.delivery.presentation.adapter.OrderProductAdapter
import com.itis.delivery.presentation.base.BaseFragment
import com.itis.delivery.presentation.model.OrderProductUiModel
import com.itis.delivery.presentation.model.OrderUiModel
import com.itis.delivery.utils.convertTimestampToDateString
import com.itis.delivery.utils.convertTimestampToDateTimeString
import com.itis.delivery.utils.getTextByOrderState
import com.itis.delivery.utils.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class OrderPageFragment : BaseFragment(R.layout.fragment_order_page) {

    private val viewBinding: FragmentOrderPageBinding
            by viewBinding(FragmentOrderPageBinding::bind)
    @Inject
    lateinit var viewModelFactory: OrderPageViewModel.Factory
    private val viewModel: OrderPageViewModel by viewModels {
        OrderPageViewModel.provideFactory(
            viewModelFactory,
            requireArguments().getLong(Keys.ORDER_NUMBER)
        )
    }

    private var orderModel: OrderUiModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe()

        viewBinding.run {
            rvProducts.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            btnCancelOrder.setOnClickListener {
                viewModel.cancelOrder()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.getOrder()
    }

    private fun setData() {
        orderModel?.let {
            with(viewBinding) {
                mtvTitleOrder.text = getString(
                    R.string.propmt_order_title,
                    convertTimestampToDateString(it.createdTime)
                )
                mtvOrderNumber.text = it.orderNumber.toString()
                mtvStatus.text = getTextByOrderState(it.state, requireContext())
                mtvLastChanges.text = convertTimestampToDateTimeString(it.lastChange)
                mtvAddress.text = it.address
            }
        }
    }

    private fun onOrderProductClicked(product: OrderProductUiModel) {
        findNavController().safeNavigate(
            R.id.orderPageFragment,
            R.id.action_orderPageFragment_to_productPageFragment,
            Bundle()
                .apply {
                    putLong(Keys.PRODUCT_ID, product.productId)
                }
        )
    }

    private fun onRatingClicked(product: OrderProductUiModel, rate: Int) {
        viewModel.rateProduct(product.productId, rate = rate)
    }

    private fun observe() {
        with(viewModel) {
            order.observe { order ->
                if (order != null) {
                    orderModel = order
                    setData()

                    viewBinding.rvProducts.adapter = OrderProductAdapter(
                        onOrderProductClick = ::onOrderProductClicked,
                        onRatingClick = ::onRatingClicked,
                        isOrderDelivered = order.state == DELIVERED
                    ).apply {
                        setList(order.products)
                    }
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
                )
            }
            isOrderCancelled.observe {
                if (it) {
                    showSnackBar(getString(R.string.prompt_successful_order_cancel))
                    findNavController().popBackStack()
                }
            }
            isRated.observe {
                if (it) {
                    showSnackBar(getString(R.string.product_rated))
                }
            }
            lifecycleScope.launch {
                setErrorVisibility(
                    root = viewBinding.root,
                    isVisible = false,
                    btnOnClickListener = { viewModel.getOrder() }
                )
            }
        }
    }
}