package com.itis.delivery.presentation.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.itis.delivery.R
import com.itis.delivery.databinding.ItemOrderBinding
import com.itis.delivery.presentation.model.OrderUiModel
import com.itis.delivery.utils.convertTimestampToDateString
import com.itis.delivery.utils.getTextByOrderState

class OrderItemHolder(
    private val binding: ItemOrderBinding,
    private val onOrderClick: (OrderUiModel) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bindItem(item: OrderUiModel) {
        with(binding) {
            mtvOrderState.text = getTextByOrderState(item.state, binding.root.context)
            mtvOrderDate.text = convertTimestampToDateString(item.createdTime)
            Glide.with(binding.root)
                .load(item.products[0].productImageUrl)
                .error(R.drawable.no_image)
                .into(ivFirst)
            if (item.products.size > 2) {
                Glide.with(binding.root)
                    .load(item.products[1].productImageUrl)
                    .error(R.drawable.no_image)
                    .into(ivSecond)
            } else {
                ivSecond.visibility = View.GONE
            }
            if (item.products.size > 3) {
                Glide.with(binding.root)
                    .load(item.products[2].productImageUrl)
                    .error(R.drawable.no_image)
                    .into(ivThird)
            } else {
                ivThird.visibility = View.GONE
            }
            binding.root.setOnClickListener {
                onOrderClick(item)
            }
        }
    }
}
