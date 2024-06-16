package com.itis.delivery.presentation.holder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.itis.delivery.R
import com.itis.delivery.databinding.ItemProductBinding
import com.itis.delivery.presentation.model.ProductUiModel
import com.itis.delivery.utils.toPrice

class ProductItemHolder(
    private val binding: ItemProductBinding,
    private val onProductClick: (ProductUiModel) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bindItem(item: ProductUiModel) {
        binding.mtvProductName.text = item.name
        Glide.with(binding.root)
            .load(item.imageUrl)
            .error(R.drawable.no_image)
            .into(binding.ivProduct)

        binding.mtvPrice.text = toPrice(item.price)

        binding.root.setOnClickListener {
            onProductClick(item)
        }
    }

}