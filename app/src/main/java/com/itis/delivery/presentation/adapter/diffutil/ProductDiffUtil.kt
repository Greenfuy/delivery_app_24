package com.itis.delivery.presentation.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.itis.delivery.presentation.model.ProductUiModel

class ProductDiffUtil(
    private val oldList: List<ProductUiModel>,
    private val newList: List<ProductUiModel>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id


    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]
}