package com.floosi.data.mapper

import com.floosi.database.entity.WalletEntity
import com.floosi.domain.model.Wallet
import com.floosi.domain.model.WalletType

object WalletMapper {

    fun WalletEntity.toDomain(): Wallet = Wallet(
        id = id,
        name = name,
        type = WalletType.valueOf(type),
        currencyCode = currencyCode,
        balance = balance,
        initialBalance = initialBalance,
        colorArgb = color,
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
        color = colorArgb,
        iconKey = iconKey,
        isArchived = isArchived,
        isExcluded = isExcluded,
        sortOrder = sortOrder,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}
