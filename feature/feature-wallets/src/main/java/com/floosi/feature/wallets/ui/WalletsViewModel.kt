package com.floosi.feature.wallets.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.floosi.domain.model.Wallet
import com.floosi.domain.repository.WalletRepository
import com.floosi.feature.wallets.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class WalletsViewModel @Inject constructor(
    private val walletRepository: WalletRepository
) : ViewModel() {

    private val _state = MutableStateFlow(WalletsState())
    val state: StateFlow<WalletsState> = _state.asStateFlow()

    private val _addEditState = MutableStateFlow(AddEditWalletState())
    val addEditState: StateFlow<AddEditWalletState> = _addEditState.asStateFlow()

    init {
        observeWallets()
    }

    private fun observeWallets() {
        viewModelScope.launch {
            walletRepository.observeActive().collect { wallets ->
                _state.update {
                    it.copy(
                        wallets = wallets,
                        totalBalance = wallets.fold(BigDecimal.ZERO) { acc, w -> acc + w.balance },
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onIntent(intent: WalletsIntent) {
        when (intent) {
            is WalletsIntent.LoadWallets -> observeWallets()
            is WalletsIntent.SetArchived -> setArchived(intent.walletId, intent.archived)
            is WalletsIntent.DeleteWallet -> deleteWallet(intent.walletId)
        }
    }

    fun onAddEditIntent(intent: AddEditWalletIntent) {
        when (intent) {
            is AddEditWalletIntent.SetName -> _addEditState.update { it.copy(name = intent.name) }
            is AddEditWalletIntent.SetType -> _addEditState.update { it.copy(type = intent.type, iconKey = intent.type.iconKey) }
            is AddEditWalletIntent.SetBalance -> _addEditState.update { it.copy(balance = intent.balance) }
            is AddEditWalletIntent.SetCurrency -> _addEditState.update { it.copy(currencyCode = intent.currencyCode) }
            is AddEditWalletIntent.SetColor -> _addEditState.update { it.copy(colorArgb = intent.colorArgb) }
            is AddEditWalletIntent.SetIcon -> _addEditState.update { it.copy(iconKey = intent.iconKey) }
            is AddEditWalletIntent.Save -> saveWallet()
            is AddEditWalletIntent.Reset -> _addEditState.value = AddEditWalletState()
        }
    }

    fun loadWalletForEdit(walletId: Long) {
        viewModelScope.launch {
            val wallet = walletRepository.getById(walletId)
            if (wallet != null) {
                _addEditState.value = AddEditWalletState(
                    isEditing = true,
                    walletId = wallet.id,
                    name = wallet.name,
                    type = wallet.type,
                    balance = if (wallet.balance > BigDecimal.ZERO) wallet.balance.toString() else "",
                    currencyCode = wallet.currencyCode,
                    colorArgb = wallet.colorArgb,
                    iconKey = wallet.iconKey
                )
            }
        }
    }

    private fun setArchived(walletId: Long, archived: Boolean) {
        viewModelScope.launch { walletRepository.setArchived(walletId, archived) }
    }

    private fun deleteWallet(walletId: Long) {
        viewModelScope.launch { walletRepository.delete(walletId) }
    }

    private fun saveWallet() {
        val current = _addEditState.value
        if (current.name.isBlank()) {
            _addEditState.update { it.copy(errorRes = R.string.wallets_name_required) }
            return
        }
        viewModelScope.launch {
            _addEditState.update { it.copy(isSaving = true, errorRes = null) }
            val now = LocalDateTime.now()
            val wallet = Wallet(
                id = current.walletId ?: 0,
                name = current.name,
                type = current.type,
                currencyCode = current.currencyCode,
                balance = current.balance.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                initialBalance = BigDecimal.ZERO,
                colorArgb = current.colorArgb,
                iconKey = current.iconKey,
                sortOrder = 0,
                createdAt = now,
                updatedAt = now
            )
            if (current.isEditing && current.walletId != null) {
                walletRepository.update(wallet)
            } else {
                walletRepository.insert(wallet)
            }
            _addEditState.update { it.copy(isSaving = false, isSaved = true) }
        }
    }
}
