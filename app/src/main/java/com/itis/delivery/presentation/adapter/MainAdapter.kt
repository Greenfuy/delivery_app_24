package com.itis.delivery.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itis.delivery.databinding.ItemCategoryListBinding
import com.itis.delivery.databinding.ItemProductListBinding
import com.itis.delivery.presentation.holder.CategoryListItemHolder
import com.itis.delivery.presentation.holder.ProductListItemHolder
import com.itis.delivery.presentation.model.CategoryUiModel
import com.itis.delivery.presentation.model.ProductUiModel

class MainAdapter(
    private val productList: List<ProductUiModel>,
    private val onSearchClick: () -> Unit,
    private val onProductClick: (ProductUiModel) -> Unit,
    private val onCategoryClick: (CategoryUiModel) -> Unit,
    private val categoryList: List<CategoryUiModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            KEY_CATEGORY -> CategoryListItemHolder(
                ItemCategoryListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
                onSearchClick,
                CategoryAdapter(onCategoryClick)
                    .also { it.setList(categoryList) }
            )
            KEY_PRODUCT -> ProductListItemHolder(
                ItemProductListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                ProductAdapter(
                    onProductClick
                ).also { it.setList(productList) }
            )
            else -> throw IllegalArgumentException("Invalid view type")
        }
    override fun getItemCount(): Int = 2

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            KEY_CATEGORY -> (holder as CategoryListItemHolder).bindItem()
            KEY_PRODUCT -> (holder as ProductListItemHolder).bindItem()
        }
    }

    override fun getItemViewType(position: Int): Int = when (position) {
        0 -> KEY_CATEGORY
        else -> KEY_PRODUCT
    }

    companion object {
        private const val KEY_CATEGORY = 0
        private const val KEY_PRODUCT = 1
    }
}