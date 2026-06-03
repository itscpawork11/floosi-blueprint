package com.floosi.data.mapper

import com.floosi.database.entity.TransactionEntity
import com.floosi.domain.model.Transaction
import com.floosi.domain.model.TransactionType

object TransactionMapper {

    fun TransactionEntity.toDomain(): Transaction = Transaction(
        id = id,
        type = TransactionType.valueOf(type),
        amount = amount,
        currencyCode = currencyCode,
        amountInBase = amountInBase,
        walletId = walletId,
        toWalletId = toWalletId,
        categoryId = categoryId,
        note = note,
        occurredAt = occurredAt,
        timezone = timezone,
        locationName = locationName,
        locationLat = locationLat,
        locationLng = locationLng,
        attachmentPath = attachmentPath,
        recurringRuleId = recurringRuleId,
        isExcluded = isExcluded,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

    fun Transaction.toEntity(): TransactionEntity = TransactionEntity(
        id = id,
        type = type.name,
        amount = amount,
        currencyCode = currencyCode,
        amountInBase = amountInBase,
        walletId = walletId,
        toWalletId = toWalletId,
        categoryId = categoryId,
        note = note,
        occurredAt = occurredAt,
        timezone = timezone,
        locationName = locationName,
        locationLat = locationLat,
        locationLng = locationLng,
        attachmentPath = attachmentPath,
        recurringRuleId = recurringRuleId,
        isExcluded = isExcluded,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}
