package com.floosi.database.entity

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "tags",
    indices = [
        Index(value = ["name"], unique = true),
    ],
)
data class TagEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "color_hex") val color: Color,
    @ColumnInfo(name = "created_at") val createdAt: LocalDateTime,
)
