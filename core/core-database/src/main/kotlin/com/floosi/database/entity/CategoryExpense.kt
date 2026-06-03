package com.floosi.database.entity

import androidx.room.ColumnInfo

data class CategoryExpense(
    @ColumnInfo(name = "category_id") val categoryId: Long?,
    @ColumnInfo(name = "total") val total: Double,
)
