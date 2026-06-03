package com.floosi.database.entity

data class DashboardRaw(
    val monthlyIncome: Double,
    val monthlyExpense: Double,
    val recentTransactions: List<TransactionEntity>,
)
