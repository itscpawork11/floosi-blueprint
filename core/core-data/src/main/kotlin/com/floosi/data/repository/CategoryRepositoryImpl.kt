package com.floosi.data.repository

import com.floosi.data.mapper.toDomain
import com.floosi.data.mapper.toEntity
import com.floosi.database.dao.CategoryDao
import com.floosi.domain.model.Category
import com.floosi.domain.model.CategoryType
import com.floosi.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
) : CategoryRepository {

    override fun observeByType(type: CategoryType): Flow<List<Category>> {
        return categoryDao.observeByType(type.name).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun observeAll(): Flow<List<Category>> {
        return categoryDao.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getById(id: Long): Category? {
        return categoryDao.getById(id)?.toDomain()
    }

    override suspend fun insert(category: Category): Long {
        return categoryDao.insert(category.toEntity())
    }

    override suspend fun update(category: Category) {
        categoryDao.update(category.toEntity())
    }

    override suspend fun setArchived(id: Long, archived: Boolean) {
        categoryDao.setArchived(id, archived)
    }

    override suspend fun delete(id: Long) {
        categoryDao.getById(id)?.let { categoryDao.delete(it) }
    }
}
