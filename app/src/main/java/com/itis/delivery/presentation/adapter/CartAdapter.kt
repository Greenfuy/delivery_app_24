package com.itis.delivery.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itis.delivery.databinding.ItemCartProductBinding
import com.itis.delivery.presentation.holder.CartProductItemHolder
import com.itis.delivery.presentation.model.CartProductModel

class CartAdapter(
    private val onItemCartProductClick: (CartProductModel) -> Unit,
    private val onChosenCheck: (Long, Boolean) -> Unit,
    private val onIncreaseCountClick: (CartProductModel) -> Unit,
    private val onDecreaseCountClick: (CartProductModel) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val cartProductList: MutableList<CartProductModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        CartProductItemHolder(
            ItemCartProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemCartProductClick,
            onChosenCheck,
            onIncreaseCountClick,
            onDecreaseCountClick
        )

    override fun getItemCount(): Int = cartProductList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CartProductItemHolder).bindItem(cartProductList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<CartProductModel>) {
        cartProductList.clear()
        cartProductList.addAll(list)
        notifyDataSetChanged()
    }

}