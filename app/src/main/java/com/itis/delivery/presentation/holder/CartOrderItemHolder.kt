package com.itis.delivery.presentation.holder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.itis.delivery.R
import com.itis.delivery.databinding.ItemCartOrderBinding
import com.itis.delivery.presentation.model.CartProductModel
import com.itis.delivery.utils.toPrice

class CartOrderItemHolder(
    private val binding: ItemCartOrderBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bindItem(item: CartProductModel) {
        with(binding) {
            mtvProductName.text = item.productName
            Glide.with(root)
                .load(item.productImageUrl)
                .error(R.drawable.no_image)
                .into(ivProduct)
            mtvCount.text = root.context.getString(R.string.prompt_items, item.count.toString())
            mtvPrice.text = toPrice(item.price)
        }
    }
}
