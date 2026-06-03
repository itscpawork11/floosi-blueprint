package com.floosi.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = WalletEntity::class,
            parentColumns = ["id"],
            childColumns = ["wallet_id"],
            onDelete = ForeignKey.RESTRICT,
        ),
        ForeignKey(
            entity = WalletEntity::class,
            parentColumns = ["id"],
            childColumns = ["to_wallet_id"],
            onDelete = ForeignKey.RESTRICT,
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.SET_NULL,
        ),
    ],
    indices = [
        Index(value = ["occurred_at"], name = "idx_transactions_date"),
        Index(value = ["wallet_id", "occurred_at"], name = "idx_transactions_wallet"),
        Index(value = ["category_id", "occurred_at"], name = "idx_transactions_category"),
        Index(value = ["type", "occurred_at"], name = "idx_transactions_type_date"),
    ],
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "amount") val amount: BigDecimal,
    @ColumnInfo(name = "currency_code") val currencyCode: String = "EGP",
    @ColumnInfo(name = "amount_in_base") val amountInBase: BigDecimal,
    @ColumnInfo(name = "wallet_id") val walletId: Long,
    @ColumnInfo(name = "to_wallet_id") val toWalletId: Long? = null,
    @ColumnInfo(name = "category_id") val categoryId: Long? = null,
    @ColumnInfo(name = "note") val note: String = "",
    @ColumnInfo(name = "occurred_at") val occurredAt: LocalDateTime,
    @ColumnInfo(name = "timezone") val timezone: String = "Africa/Cairo",
    @ColumnInfo(name = "location_name") val locationName: String? = null,
    @ColumnInfo(name = "location_lat") val locationLat: Double? = null,
    @ColumnInfo(name = "location_lng") val locationLng: Double? = null,
    @ColumnInfo(name = "attachment_path") val attachmentPath: String? = null,
    @ColumnInfo(name = "recurring_rule_id") val recurringRuleId: Long? = null,
    @ColumnInfo(name = "is_excluded") val isExcluded: Boolean = false,
    @ColumnInfo(name = "created_at") val createdAt: LocalDateTime,
    @ColumnInfo(name = "updated_at") val updatedAt: LocalDateTime,
)
