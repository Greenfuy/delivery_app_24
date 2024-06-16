package com.itis.delivery.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itis.delivery.databinding.ItemOrderProductBinding
import com.itis.delivery.presentation.holder.OrderProductItemHolder
import com.itis.delivery.presentation.model.OrderProductUiModel

class OrderProductAdapter(
    private val onOrderProductClick: (OrderProductUiModel) -> Unit,
    private val onRatingClick: (OrderProductUiModel, Int) -> Unit,
    private val isOrderDelivered: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val orderProductList = mutableListOf<OrderProductUiModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        OrderProductItemHolder(
            ItemOrderProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onOrderProductClick = onOrderProductClick,
            onRatingClick = onRatingClick,
            isOrderDelivered = isOrderDelivered
        )

    override fun getItemCount(): Int = orderProductList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as OrderProductItemHolder).bindItem(orderProductList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<OrderProductUiModel>) {
        orderProductList.clear()
        orderProductList.addAll(list)
        notifyDataSetChanged()
    }
}