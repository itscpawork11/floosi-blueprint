package com.floosi.feature.categories.ui

import com.floosi.domain.model.Category
import com.floosi.domain.model.CategoryType

data class CategoriesState(
    val categories: List<Category> = emptyList(),
    val selectedType: CategoryType = CategoryType.EXPENSE,
    val isLoading: Boolean = true,
)

sealed interface CategoriesIntent {
    data class SelectType(val type: CategoryType) : CategoriesIntent
    data object Refresh : CategoriesIntent
}
