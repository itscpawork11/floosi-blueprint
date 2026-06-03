package com.floosi.feature.transactions.ui

import com.floosi.domain.model.Transaction
import com.floosi.domain.model.TransactionType
import java.time.LocalDate

data class TransactionsState(
    val groupedTransactions: Map<LocalDate, List<Transaction>> = emptyMap(),
    val filter: TransactionFilter = TransactionFilter.ALL,
    val isLoading: Boolean = true
)

enum class TransactionFilter {
    ALL, EXPENSE, INCOME, TRANSFER
}

data class QuickAddState(
    val type: TransactionType = TransactionType.EXPENSE,
    val amount: String = "",
    val categoryId: Long? = null,
    val walletId: Long? = null,
    val note: String = "",
    val date: LocalDate = LocalDate.now(),
    val isSaved: Boolean = false,
    val isSaving: Boolean = false,
    val errorRes: Int? = null
)

sealed interface TransactionsIntent {
    data class SetFilter(val filter: TransactionFilter) : TransactionsIntent
    data class DeleteTransaction(val transactionId: Long) : TransactionsIntent
    data class SelectTransaction(val transactionId: Long) : TransactionsIntent
    data object ClearSelection : TransactionsIntent
}

sealed interface QuickAddIntent {
    data class SetType(val type: TransactionType) : QuickAddIntent
    data class SetAmount(val amount: String) : QuickAddIntent
    data class SetCategory(val categoryId: Long) : QuickAddIntent
    data class SetWallet(val walletId: Long) : QuickAddIntent
    data class SetNote(val note: String) : QuickAddIntent
    data class SetDate(val date: LocalDate) : QuickAddIntent
    data object Save : QuickAddIntent
    data object Reset : QuickAddIntent
}
