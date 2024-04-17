package com.itis.delivery.presentation.holder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.itis.delivery.R
import com.itis.delivery.databinding.ItemProductBinding
import com.itis.delivery.presentation.model.ProductUiModel

class ProductItemHolder(
    private val binding: ItemProductBinding,
    private val onProductClick: (ProductUiModel) -> Unit,
    private val onProductToCartClick: (ProductUiModel) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bindItem(item: ProductUiModel) {
        binding.mtvProductName.text = item.name
        Glide.with(binding.root)
            .load(item.imageUrl)
            .error(R.drawable.no_image)
            .into(binding.ivProduct)

        binding.root.setOnClickListener {
            onProductClick(item)
        }
        binding.btnCart.setOnClickListener {
            onProductToCartClick(item)
        }
    }

    fun changeInCartButtonStatus(status: Boolean) {
        binding.btnCart.isChecked = status
    }
}