package com.floosi.domain.repository

import com.floosi.domain.model.Category
import com.floosi.domain.model.CategoryType
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun observeByType(type: CategoryType): Flow<List<Category>>
    fun observeAll(): Flow<List<Category>>
    suspend fun getById(id: Long): Category?
    suspend fun insert(category: Category): Long
    suspend fun update(category: Category)
    suspend fun setArchived(id: Long, archived: Boolean)
    suspend fun delete(id: Long)
}
