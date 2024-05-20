package com.itis.delivery.presentation.screens.search

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.itis.delivery.R
import com.itis.delivery.base.Keys.PRICE_END
import com.itis.delivery.base.Keys.PRICE_START
import com.itis.delivery.base.Keys.RATE
import com.itis.delivery.databinding.FragmentSortBinding
import com.itis.delivery.presentation.base.BaseFragment
import com.itis.delivery.utils.safeNavigate

class SortFragment : BaseFragment(R.layout.fragment_sort) {

    private val viewBinding: FragmentSortBinding by viewBinding(FragmentSortBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.run {
            btnReturn.setOnClickListener {
                findNavController().popBackStack()
            }

            etPriceStart.addTextChangedListener {
                validatePrices()
            }

            etPriceEnd.addTextChangedListener {
                validatePrices()
            }

            btnSubmit.setOnClickListener {
                val priceStart = etPriceStart.text.toString().toIntOrNull() ?: 0
                val priceEnd = etPriceEnd.text.toString().toIntOrNull() ?: 0
                val rate = when (rgRating.checkedRadioButtonId) {
                    0 -> 3
                    1 -> 4
                    else -> 0
                }



                findNavController().safeNavigate(
                    R.id.sortFragment,
                    R.id.action_sortFragment_to_searchFragment,
                    Bundle().apply {
                            putInt(RATE, rate)
                            putInt(PRICE_START, priceStart)
                            putInt(PRICE_END, priceEnd)
                    }
                )
            }
        }
    }

    private fun validatePrices() {
        with(viewBinding) {
            val priceStartText = etPriceStart.text.toString()
            val priceEndText = etPriceEnd.text.toString()

            val priceStart = priceStartText.toIntOrNull() ?: 0
            val priceEnd = priceEndText.toIntOrNull() ?: 0

            when {
                priceStart < 0 || priceEnd < 0 || (priceEnd in 1..<priceStart) -> {
                    etPriceStart.error = "Wrong data"
                    etPriceEnd.error = "Wrong data"
                    btnSubmit.isEnabled = false
                }
                else -> {
                    etPriceStart.error = null
                    etPriceEnd.error = null
                    btnSubmit.isEnabled = true
                }
            }
        }
    }
}