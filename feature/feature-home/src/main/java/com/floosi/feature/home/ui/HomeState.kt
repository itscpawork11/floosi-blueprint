package com.floosi.feature.home.ui

import com.floosi.domain.model.Transaction
import java.math.BigDecimal

data class HomeState(
    val totalBalance: BigDecimal = BigDecimal.ZERO,
    val monthlyIncome: BigDecimal = BigDecimal.ZERO,
    val monthlyExpense: BigDecimal = BigDecimal.ZERO,
    val recentTransactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = true,
)

sealed interface HomeIntent {
    data object Refresh : HomeIntent
}
