package com.floosi.feature.transactions.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.floosi.domain.model.Transaction
import com.floosi.domain.model.TransactionType
import com.floosi.domain.repository.TransactionRepository
import com.floosi.domain.repository.WalletRepository
import com.floosi.feature.transactions.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class QuickAddViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val walletRepository: WalletRepository
) : ViewModel() {

    private val _state = MutableStateFlow(QuickAddState())
    val state: StateFlow<QuickAddState> = _state.asStateFlow()

    init {
        loadDefaults()
    }

    private fun loadDefaults() {
        viewModelScope.launch {
            val wallets = walletRepository.observeActive().first()
            val defaultWallet = wallets.firstOrNull()
            if (defaultWallet != null) {
                _state.update { it.copy(walletId = defaultWallet.id) }
            }
        }
    }

    fun onIntent(intent: QuickAddIntent) {
        when (intent) {
            is QuickAddIntent.SetType -> _state.update { it.copy(type = intent.type, categoryId = null) }
            is QuickAddIntent.SetAmount -> {
                if (intent.amount.all { c -> c.isDigit() || c == '.' }) {
                    _state.update { it.copy(amount = intent.amount) }
                }
            }
            is QuickAddIntent.SetCategory -> _state.update { it.copy(categoryId = intent.categoryId) }
            is QuickAddIntent.SetWallet -> _state.update { it.copy(walletId = intent.walletId) }
            is QuickAddIntent.SetNote -> _state.update { it.copy(note = intent.note) }
            is QuickAddIntent.SetDate -> _state.update { it.copy(date = intent.date) }
            is QuickAddIntent.Save -> save()
            is QuickAddIntent.Reset -> {
                _state.value = QuickAddState(walletId = _state.value.walletId)
            }
        }
    }

    private fun save() {
        val current = _state.value
        if (current.amount.isBlank()) {
            _state.update { it.copy(errorRes = R.string.quickadd_amount_required) }
            return
        }
        val amount = current.amount.toBigDecimalOrNull()
        if (amount == null || amount <= BigDecimal.ZERO) {
            _state.update { it.copy(errorRes = R.string.quickadd_invalid_amount) }
            return
        }
        if (current.walletId == null) {
            _state.update { it.copy(errorRes = R.string.quickadd_wallet_required) }
            return
        }

        val now = LocalDateTime.now()
        val signedAmount = if (current.type == TransactionType.EXPENSE) amount.negate() else amount

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, errorRes = null) }
            val transaction = Transaction(
                type = current.type,
                amount = signedAmount,
                currencyCode = "EGP",
                amountInBase = signedAmount,
                walletId = current.walletId,
                toWalletId = if (current.type == TransactionType.TRANSFER) current.walletId else null,
                categoryId = current.categoryId,
                note = current.note,
                occurredAt = current.date.atStartOfDay(),
                createdAt = now,
                updatedAt = now
            )
            transactionRepository.insert(transaction)
            _state.update {
                it.copy(isSaving = false, isSaved = true, amount = "", categoryId = null, note = "")
            }
        }
    }
}
