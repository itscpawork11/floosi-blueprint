package com.floosi.domain.model

import java.math.BigDecimal

data class Dashboard(
    val totalBalance: BigDecimal = BigDecimal.ZERO,
    val monthlyIncome: BigDecimal = BigDecimal.ZERO,
    val monthlyExpense: BigDecimal = BigDecimal.ZERO,
    val recentTransactions: List<Transaction> = emptyList(),
    val expensesByCategory: Map<Long, BigDecimal> = emptyMap(),
)
