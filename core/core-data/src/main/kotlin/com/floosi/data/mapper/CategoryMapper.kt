package com.floosi.data.mapper

import com.floosi.database.entity.CategoryEntity
import com.floosi.domain.model.Category
import com.floosi.domain.model.CategoryType

object CategoryMapper {

    fun CategoryEntity.toDomain(): Category = Category(
        id = id,
        name = name,
        type = CategoryType.valueOf(type),
        iconKey = iconKey,
        colorArgb = color,
        parentId = parentId,
        isSystem = isSystem,
        isArchived = isArchived,
        sortOrder = sortOrder,
        keywords = keywords,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

    fun Category.toEntity(): CategoryEntity = CategoryEntity(
        id = id,
        name = name,
        type = type.name,
        iconKey = iconKey,
        color = colorArgb,
        parentId = parentId,
        isSystem = isSystem,
        isArchived = isArchived,
        sortOrder = sortOrder,
        keywords = keywords,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}
