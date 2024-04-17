package com.itis.delivery.data.repository

import com.itis.delivery.R
import com.itis.delivery.domain.repository.CategoryRepository
import com.itis.delivery.presentation.model.CategoryUiModel
import com.itis.delivery.utils.ResManager
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(resManager: ResManager) : CategoryRepository {

    private val categoryList = mutableListOf(
        CategoryUiModel(0, "Fruits", resManager.getDrawable(R.drawable.no_image)),
        CategoryUiModel(1, "Meats", resManager.getDrawable(R.drawable.no_image)),
        CategoryUiModel(2, "Vegetables", resManager.getDrawable(R.drawable.no_image)),
        CategoryUiModel(3, "Fish and seafood", resManager.getDrawable(R.drawable.no_image)),
        CategoryUiModel(4, "Dairy desserts", resManager.getDrawable(R.drawable.no_image)),
        CategoryUiModel(5, "Eggs", resManager.getDrawable(R.drawable.no_image)),
        CategoryUiModel(6, "Cereal rains and bakery products", resManager.getDrawable(R.drawable.no_image)),
        CategoryUiModel(7, "Pastas", resManager.getDrawable(R.drawable.no_image)),
        CategoryUiModel(8, "Nuts and dried fruits", resManager.getDrawable(R.drawable.no_image)),
        CategoryUiModel(9, "Spices", resManager.getDrawable(R.drawable.no_image)),
        CategoryUiModel(10, "Vegetable oils and sauces", resManager.getDrawable(R.drawable.no_image)),
        CategoryUiModel(11, "Sweets and confectionery", resManager.getDrawable(R.drawable.no_image)),
        CategoryUiModel(12, "Non-alcoholic beverages", resManager.getDrawable(R.drawable.no_image)),
        CategoryUiModel(13, "Coffee and tea", resManager.getDrawable(R.drawable.no_image)),
        CategoryUiModel(14, "Baby food", resManager.getDrawable(R.drawable.no_image))
    )

    override fun getCategories(): List<CategoryUiModel> = categoryList
}