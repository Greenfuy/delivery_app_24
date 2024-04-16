package com.itis.delivery.presentation.holder

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.itis.delivery.databinding.ItemProductListBinding
import com.itis.delivery.presentation.adapter.ProductAdapter

class ProductListItemHolder(
    private val binding: ItemProductListBinding,
    private val adapter: ProductAdapter
) : RecyclerView.ViewHolder(binding.root) {

    fun bindItem() {
        binding.rvProducts.adapter = adapter
        binding.rvProducts.layoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )
    }
}