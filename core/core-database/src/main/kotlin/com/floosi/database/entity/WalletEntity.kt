package com.floosi.database.entity

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(
    tableName = "wallets",
    indices = [
        Index(value = ["is_archived", "sort_order"], name = "idx_wallets_active"),
    ],
)
data class WalletEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "currency_code") val currencyCode: String = "EGP",
    @ColumnInfo(name = "balance") val balance: BigDecimal = BigDecimal.ZERO,
    @ColumnInfo(name = "initial_balance") val initialBalance: BigDecimal = BigDecimal.ZERO,
    @ColumnInfo(name = "color_hex") val color: Color,
    @ColumnInfo(name = "icon_key") val iconKey: String,
    @ColumnInfo(name = "is_archived") val isArchived: Boolean = false,
    @ColumnInfo(name = "is_excluded") val isExcluded: Boolean = false,
    @ColumnInfo(name = "sort_order") val sortOrder: Int = 0,
    @ColumnInfo(name = "created_at") val createdAt: LocalDateTime,
    @ColumnInfo(name = "updated_at") val updatedAt: LocalDateTime,
)
