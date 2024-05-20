package com.itis.delivery.presentation.screens.search

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.itis.delivery.R
import com.itis.delivery.base.Keys
import com.itis.delivery.base.Keys.PRICE_END
import com.itis.delivery.base.Keys.PRICE_START
import com.itis.delivery.base.Keys.RATE
import com.itis.delivery.databinding.FragmentSearchBinding
import com.itis.delivery.presentation.base.BaseFragment
import com.itis.delivery.utils.safeNavigate

class SearchFragment : BaseFragment(R.layout.fragment_search) {

    private val viewBinding: FragmentSearchBinding by viewBinding(FragmentSearchBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.run {
            val rate = arguments?.getInt(RATE) ?: 0
            val priceStart = arguments?.getInt(PRICE_START) ?: 0
            val priceEnd = arguments?.getInt(PRICE_END) ?: 0


            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (!query.isNullOrEmpty()) {
                        findNavController().safeNavigate(
                            R.id.searchFragment,
                            R.id.action_searchFragment_to_resultSearchFragment,
                            Bundle().apply {
                                putString(Keys.SEARCH_TERM, query)
                                putInt(RATE, rate)
                                putInt(PRICE_START, priceStart)
                                putInt(PRICE_END, priceEnd)
                            }
                        )
                        Log.d("SearchFragment",
                            "to resultSearchFragment: $query, rate: $rate, priceStart: " +
                                    "$priceStart, priceEnd: $priceEnd")
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            })

            ibSort.setOnClickListener {
                findNavController().safeNavigate(
                    R.id.searchFragment,
                    R.id.action_searchFragment_to_sortFragment
                )
            }
        }
    }
}