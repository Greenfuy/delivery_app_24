package com.itis.delivery.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itis.delivery.databinding.ItemCartOrderBinding
import com.itis.delivery.presentation.holder.CartOrderItemHolder
import com.itis.delivery.presentation.model.CartProductModel

class CartOrderAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val cartProductList = mutableListOf<CartProductModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        CartOrderItemHolder(
            ItemCartOrderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
        )

    override fun getItemCount(): Int = cartProductList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CartOrderItemHolder).bindItem(cartProductList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<CartProductModel>) {
        cartProductList.clear()
        cartProductList.addAll(list)
        notifyDataSetChanged()
    }
}