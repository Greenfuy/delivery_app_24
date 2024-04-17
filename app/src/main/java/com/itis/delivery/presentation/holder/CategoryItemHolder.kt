package com.itis.delivery.presentation.holder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.itis.delivery.R
import com.itis.delivery.databinding.ItemCategoryBinding
import com.itis.delivery.presentation.model.CategoryUiModel

class CategoryItemHolder(
    private val binding: ItemCategoryBinding,
    private val onCategoryClick: (CategoryUiModel) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bindItem(item: CategoryUiModel) {
        binding.mtvCategoryName.text = item.name
        Glide.with(binding.root)
            .load(item.icon)
            .error(R.drawable.no_image)
            .into(binding.ivCategory)
        binding.root.setOnClickListener { onCategoryClick(item) }
    }
}