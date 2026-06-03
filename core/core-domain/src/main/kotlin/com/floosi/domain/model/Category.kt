package com.floosi.domain.model

import java.time.LocalDateTime

data class Category(
    val id: Long = 0,
    val name: String,
    val type: CategoryType,
    val iconKey: String,
    val colorArgb: Int,
    val parentId: Long? = null,
    val isSystem: Boolean = false,
    val isArchived: Boolean = false,
    val sortOrder: Int = 0,
    val keywords: List<String> = emptyList(),
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
