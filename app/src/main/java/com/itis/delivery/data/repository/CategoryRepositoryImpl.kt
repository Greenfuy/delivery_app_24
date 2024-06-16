package com.itis.delivery.data.repository

import com.itis.delivery.R
import com.itis.delivery.domain.repository.CategoryRepository
import com.itis.delivery.presentation.model.CategoryUiModel
import com.itis.delivery.utils.ResManager
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(resManager: ResManager) : CategoryRepository {

    private val categoryList = mutableListOf(
        CategoryUiModel(1, Categories.MEATS, resManager.getDrawable(R.drawable.no_image)),
        CategoryUiModel(3, Categories.FISH_AND_SEAFOOD, resManager.getDrawable(R.drawable.no_image)),
        CategoryUiModel(4, Categories.DAIRY_DESSERTS, resManager.getDrawable(R.drawable.no_image)),
        CategoryUiModel(5, Categories.EGGS, resManager.getDrawable(R.drawable.no_image)),
        CategoryUiModel(6, Categories.CEREAL_GRAINS_AND_BAKERY_PRODUCTS, resManager.getDrawable(R.drawable.no_image)),
        CategoryUiModel(7, Categories.PASTAS, resManager.getDrawable(R.drawable.no_image)),
        CategoryUiModel(8, Categories.NUTS_AND_DRIED_FRUITS, resManager.getDrawable(R.drawable.no_image)),
        CategoryUiModel(9, Categories.SPICES, resManager.getDrawable(R.drawable.no_image)),
        CategoryUiModel(10, Categories.VEGETABLE_OILS_AND_SAUCES, resManager.getDrawable(R.drawable.no_image)),
        CategoryUiModel(11, Categories.SWEETS_AND_CONFECTIONERY, resManager.getDrawable(R.drawable.no_image)),
        CategoryUiModel(12, Categories.NON_ALCOHOLIC_BEVERAGES, resManager.getDrawable(R.drawable.no_image)),
        CategoryUiModel(13, Categories.COFFEE_AND_TEA, resManager.getDrawable(R.drawable.no_image)),
        CategoryUiModel(14, Categories.BABY_FOODS, resManager.getDrawable(R.drawable.no_image))
    )


    override fun getCategories(): List<CategoryUiModel> = categoryList

    object Categories {
        const val MEATS = "Meats"
        const val FISH_AND_SEAFOOD = "Fish and Seafood"
        const val DAIRY_DESSERTS = "Dairy Desserts"
        const val EGGS = "Eggs"
        const val CEREAL_GRAINS_AND_BAKERY_PRODUCTS = "Cereal Grains and Bakery Products"
        const val PASTAS = "Pastas"
        const val NUTS_AND_DRIED_FRUITS = "Nuts and Dried Fruits"
        const val SPICES = "Spices"
        const val VEGETABLE_OILS_AND_SAUCES = "Vegetable Oils and Sauces"
        const val SWEETS_AND_CONFECTIONERY = "Sweets and Confectionery"
        const val NON_ALCOHOLIC_BEVERAGES = "Non-Alcoholic Beverages"
        const val COFFEE_AND_TEA = "Coffee and Tea"
        const val BABY_FOODS = "Baby Foods"
    }
}