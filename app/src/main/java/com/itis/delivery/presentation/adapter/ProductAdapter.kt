package com.itis.delivery.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.itis.delivery.databinding.ItemProductBinding
import com.itis.delivery.presentation.adapter.diffutil.ProductDiffUtil
import com.itis.delivery.presentation.holder.ProductItemHolder
import com.itis.delivery.presentation.model.ProductUiModel

class ProductAdapter(
    private val onProductClick: (ProductUiModel) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var productList = mutableListOf<ProductUiModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ProductItemHolder(
            ItemProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onProductClick
        )

    override fun getItemCount(): Int = productList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ProductItemHolder).bindItem(productList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<ProductUiModel>) {
        val diff = ProductDiffUtil(oldList = productList, newList = list)
        val diffResult = DiffUtil.calculateDiff(diff)
        productList.clear()
        productList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }
}