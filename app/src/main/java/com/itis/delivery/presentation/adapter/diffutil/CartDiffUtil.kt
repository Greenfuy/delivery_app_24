package com.itis.delivery.presentation.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.itis.delivery.presentation.model.CartProductModel

class CartDiffUtil(
    private val oldList: List<CartProductModel>,
    private val newList: List<CartProductModel>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.price == newItem.price
    }


    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]


    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        if (oldItem.price != newItem.price) {
            return newItem
        }

        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}
