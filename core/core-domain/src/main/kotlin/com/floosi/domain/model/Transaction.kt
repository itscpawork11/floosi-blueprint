package com.floosi.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class Transaction(
    val id: Long = 0,
    val type: TransactionType,
    val amount: BigDecimal,
    val currencyCode: String = "EGP",
    val amountInBase: BigDecimal,
    val walletId: Long,
    val toWalletId: Long? = null,
    val categoryId: Long? = null,
    val note: String = "",
    val occurredAt: LocalDateTime,
    val timezone: String = "Africa/Cairo",
    val locationName: String? = null,
    val locationLat: Double? = null,
    val locationLng: Double? = null,
    val attachmentPath: String? = null,
    val recurringRuleId: Long? = null,
    val isExcluded: Boolean = false,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
