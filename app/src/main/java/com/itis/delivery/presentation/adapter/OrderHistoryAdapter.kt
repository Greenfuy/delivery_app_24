package com.itis.delivery.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itis.delivery.databinding.ItemOrderBinding
import com.itis.delivery.presentation.holder.OrderItemHolder
import com.itis.delivery.presentation.model.OrderUiModel

class OrderHistoryAdapter(
    private val onOrderClick: (OrderUiModel) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val orderList = mutableListOf<OrderUiModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        OrderItemHolder(
            ItemOrderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onOrderClick
        )

    override fun getItemCount(): Int = orderList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as OrderItemHolder).bindItem(orderList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addOrders(list: List<OrderUiModel>) {
        orderList.addAll(list)
        notifyDataSetChanged()
    }
}