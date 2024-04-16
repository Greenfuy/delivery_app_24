package com.itis.delivery.presentation.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.itis.delivery.presentation.model.CategoryUiModel

class CategoryDiffUtil(
    private val oldList: List<CategoryUiModel>,
    private val newList: List<CategoryUiModel>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].pos == newList[newItemPosition].pos


    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]
}