package com.itis.delivery.presentation.holder

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.itis.delivery.databinding.ItemCategoryListBinding
import com.itis.delivery.presentation.adapter.CategoryAdapter

class CategoryListItemHolder(
    private val binding: ItemCategoryListBinding,
    private val onSearchClick: () -> Unit,
    private val adapter: CategoryAdapter
) : RecyclerView.ViewHolder(binding.root) {

    fun bindItem() {
        binding.sbSearch.setOnClickListener {
            onSearchClick
        }

        binding.rvCategories.adapter = adapter
        binding.rvCategories.layoutManager = StaggeredGridLayoutManager(
            1,
            StaggeredGridLayoutManager.HORIZONTAL
        )
    }
}
