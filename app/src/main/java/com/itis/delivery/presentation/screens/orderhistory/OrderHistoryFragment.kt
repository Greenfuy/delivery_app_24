package com.itis.delivery.presentation.screens.orderhistory

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.itis.delivery.R
import com.itis.delivery.base.Keys.ORDER_NUMBER
import com.itis.delivery.data.exceptions.UserNotAuthorizedException
import com.itis.delivery.databinding.FragmentOrderHistoryBinding
import com.itis.delivery.presentation.adapter.OrderHistoryAdapter
import com.itis.delivery.presentation.base.BaseFragment
import com.itis.delivery.presentation.model.OrderUiModel
import com.itis.delivery.utils.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderHistoryFragment : BaseFragment(R.layout.fragment_order_history) {

    private val viewModel: OrderHistoryViewModel by viewModels()
    private val viewBinding: FragmentOrderHistoryBinding
    by viewBinding(FragmentOrderHistoryBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe()

        viewBinding.run {
            rvOrderHistory.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            swipeRefresh.setOnRefreshListener {
                viewModel.getOrderList()
            }

            btnReturn.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.getOrderList()
    }

    private fun onOrderClicked(order: OrderUiModel) {
        findNavController().safeNavigate(
            R.id.orderHistoryFragment,
            R.id.action_orderHistoryFragment_to_orderPageFragment,
            Bundle().apply { putLong(ORDER_NUMBER, order.orderNumber) }
        )
    }

    private fun setEmptyHistoryTextVisibility(isVisible: Boolean) {
        viewBinding.toolbar.visibility = if (isVisible) View.GONE else View.VISIBLE
        viewBinding.rvOrderHistory.visibility = if (isVisible) View.GONE else View.VISIBLE
        viewBinding.mtvEmptyOrderHistory.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun observe() {
        with(viewModel) {
            orderList.observe {
                if (it.isNotEmpty()) {
                    viewBinding.swipeRefresh.isRefreshing = false
                    viewBinding.rvOrderHistory.adapter = OrderHistoryAdapter(
                        onOrderClick = ::onOrderClicked
                    ).apply { addOrders(it.sortedBy { o -> o.state }) }
                    setEmptyHistoryTextVisibility(false)
                } else {
                    setEmptyHistoryTextVisibility(true)
                }
            }
            isLoading.observe {
                setLoadingVisibility(
                    root = viewBinding.root,
                    isVisible = it
                )
                setErrorVisibility(
                    root = viewBinding.root,
                    isVisible = it
                )
            }
            lifecycleScope.launch {
                errorsChannel.consumeEach {
                    if (it is UserNotAuthorizedException) showSignInSnackBar()
                    else setErrorVisibility(
                        root = viewBinding.swipeRefresh,
                        isVisible = true,
                        btnOnClickListener = { viewModel.getOrderList() }
                    )
                }
            }
        }
    }
}