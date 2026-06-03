package com.floosi.feature.transactions.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.floosi.domain.model.Transaction
import com.floosi.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _state = MutableStateFlow(TransactionsState())
    val state: StateFlow<TransactionsState> = _state.asStateFlow()

    init {
        observeTransactions()
    }

    private fun observeTransactions() {
        viewModelScope.launch {
            transactionRepository.observeAll().collect { transactions ->
                val filtered = when (_state.value.filter) {
                    TransactionFilter.ALL -> transactions
                    TransactionFilter.EXPENSE -> transactions.filter { it.type == com.floosi.domain.model.TransactionType.EXPENSE }
                    TransactionFilter.INCOME -> transactions.filter { it.type == com.floosi.domain.model.TransactionType.INCOME }
                    TransactionFilter.TRANSFER -> transactions.filter { it.type == com.floosi.domain.model.TransactionType.TRANSFER }
                }
                val grouped = filtered
                    .filter { !it.isExcluded }
                    .groupBy { it.occurredAt.toLocalDate() }
                    .toSortedMap(compareByDescending { it })
                _state.update {
                    it.copy(
                        groupedTransactions = grouped,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onIntent(intent: TransactionsIntent) {
        when (intent) {
            is TransactionsIntent.SetFilter -> setFilter(intent.filter)
            is TransactionsIntent.DeleteTransaction -> deleteTransaction(intent.transactionId)
            is TransactionsIntent.SelectTransaction -> { }
            is TransactionsIntent.ClearSelection -> { }
        }
    }

    private fun setFilter(filter: TransactionFilter) {
        _state.update { it.copy(filter = filter, isLoading = true) }
        observeTransactions()
    }

    private fun deleteTransaction(transactionId: Long) {
        viewModelScope.launch {
            transactionRepository.delete(transactionId)
        }
    }
}
