package com.itis.delivery.domain.repository

import com.itis.delivery.presentation.model.CategoryUiModel

interface CategoryRepository {

    fun getCategories(): List<CategoryUiModel>
}