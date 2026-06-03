package com.floosi.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class Wallet(
    val id: Long = 0,
    val name: String,
    val type: WalletType,
    val currencyCode: String = "EGP",
    val balance: BigDecimal = BigDecimal.ZERO,
    val initialBalance: BigDecimal = BigDecimal.ZERO,
    val colorArgb: Int,
    val iconKey: String,
    val isArchived: Boolean = false,
    val isExcluded: Boolean = false,
    val sortOrder: Int = 0,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
