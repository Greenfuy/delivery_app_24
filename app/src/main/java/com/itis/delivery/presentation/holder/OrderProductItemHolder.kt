package com.itis.delivery.presentation.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.itis.delivery.R
import com.itis.delivery.databinding.ItemOrderProductBinding
import com.itis.delivery.presentation.model.OrderProductUiModel
import com.itis.delivery.utils.toPrice

class OrderProductItemHolder(
    private val binding: ItemOrderProductBinding,
    private val onOrderProductClick: (OrderProductUiModel) -> Unit,
    private val onRatingClick: (OrderProductUiModel, Int) -> Unit,
    private val isOrderDelivered: Boolean
) : RecyclerView.ViewHolder(binding.root) {

    fun bindItem(item: OrderProductUiModel) {
        with(binding) {
            mtvProductName.text = item.name
            Glide.with(root)
                .load(item.productImageUrl)
                .error(R.drawable.no_image)
                .into(ivProduct)
            mtvCount.text = root.context.getString(R.string.prompt_items, item.count.toString())
            mtvPrice.text = toPrice(item.price.toInt())
            ratingbar.visibility = if (isOrderDelivered) View.VISIBLE else View.GONE
            ratingbar.setOnRatingBarChangeListener { _, rating, _ ->
                onRatingClick(item, rating.toInt())
                ratingbar.visibility = View.GONE
            }
            root.setOnClickListener {
                onOrderProductClick(item)
            }
        }
    }
}
