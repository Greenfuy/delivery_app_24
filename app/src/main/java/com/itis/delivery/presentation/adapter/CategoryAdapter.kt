package com.itis.delivery.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.itis.delivery.databinding.ItemCategoryBinding
import com.itis.delivery.presentation.adapter.diffutil.CategoryDiffUtil
import com.itis.delivery.presentation.holder.CategoryItemHolder
import com.itis.delivery.presentation.model.CategoryUiModel

class CategoryAdapter(
    private val onCategoryClick: (CategoryUiModel) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val categoryList = mutableListOf<CategoryUiModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        CategoryItemHolder(
            ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onCategoryClick
        )

    override fun getItemCount(): Int = categoryList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CategoryItemHolder).bindItem(categoryList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<CategoryUiModel>) {
        val diff = CategoryDiffUtil(oldList = categoryList, newList = list)
        val diffResult = DiffUtil.calculateDiff(diff)
        categoryList.clear()
        categoryList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

}