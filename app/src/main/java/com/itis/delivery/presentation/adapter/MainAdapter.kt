package com.itis.delivery.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itis.delivery.databinding.ItemCategoryListBinding
import com.itis.delivery.databinding.ItemProductBinding
import com.itis.delivery.databinding.ItemSearchBinding
import com.itis.delivery.presentation.holder.CategoryListItemHolder
import com.itis.delivery.presentation.holder.ProductItemHolder
import com.itis.delivery.presentation.holder.SearchItemHolder
import com.itis.delivery.presentation.model.CategoryUiModel
import com.itis.delivery.presentation.model.ProductUiModel

class MainAdapter(
    private val productList: MutableList<ProductUiModel>,
    private val onSearchClick: (searchTerm: String) -> Unit,
    private val onProductClick: (ProductUiModel) -> Unit,
    private val onCategoryClick: (CategoryUiModel) -> Unit,
    private val categoryList: List<CategoryUiModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            KEY_SEARCH -> SearchItemHolder(
                ItemSearchBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                onSearchClick
            )
            KEY_CATEGORY -> CategoryListItemHolder(
                ItemCategoryListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
                CategoryAdapter(onCategoryClick).apply { setList(categoryList) }
            )
            else -> ProductItemHolder(
                ItemProductBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                onProductClick
            )
        }
    override fun getItemCount(): Int = productList.size + 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            KEY_SEARCH -> (holder as SearchItemHolder).bindItem()
            KEY_CATEGORY -> (holder as CategoryListItemHolder).bindItem()
            else -> (holder as ProductItemHolder).bindItem(productList[position - 1])
        }
    }

    override fun getItemViewType(position: Int): Int = when (position) {
        0 -> KEY_SEARCH
        1 -> KEY_CATEGORY
        else -> position
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addProducts(list: List<ProductUiModel>) {
        productList.addAll(list)
        notifyDataSetChanged()
    }

    companion object {
        const val KEY_SEARCH = 0
        const val KEY_CATEGORY = 1
    }
}