package com.floosi.data.mapper

import androidx.compose.ui.graphics.Color
import com.floosi.database.entity.WalletEntity
import com.floosi.domain.model.Wallet
import com.floosi.domain.model.WalletType
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

object WalletMapper {

    fun WalletEntity.toDomain(): Wallet = Wallet(
        id = id,
        name = name,
        type = WalletType.valueOf(type),
        currencyCode = currencyCode,
        balance = balance,
        initialBalance = initialBalance,
        colorArgb = color.toArgb(),
        iconKey = iconKey,
        isArchived = isArchived,
        isExcluded = isExcluded,
        sortOrder = sortOrder,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

    fun Wallet.toEntity(): WalletEntity = WalletEntity(
        id = id,
        name = name,
        type = type.name,
        currencyCode = currencyCode,
        balance = balance,
        initialBalance = initialBalance,
        color = Color(colorArgb),
        iconKey = iconKey,
        isArchived = isArchived,
        isExcluded = isExcluded,
        sortOrder = sortOrder,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}
