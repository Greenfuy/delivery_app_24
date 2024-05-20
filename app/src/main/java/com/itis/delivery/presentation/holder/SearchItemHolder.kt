package com.itis.delivery.presentation.holder

import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.itis.delivery.databinding.ItemSearchBinding

class SearchItemHolder(
    private val binding: ItemSearchBinding,
    private val onSearchClick: (searchTerm: String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bindItem() {
       with(binding) {
           searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
               override fun onQueryTextSubmit(query: String?): Boolean {
                   if (!query.isNullOrEmpty()) {
                       onSearchClick(query)
                   }
                   return true
               }

               override fun onQueryTextChange(newText: String?): Boolean {
                   return true
               }

           })
       }
    }
}