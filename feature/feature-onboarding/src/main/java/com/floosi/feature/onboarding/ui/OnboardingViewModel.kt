package com.floosi.feature.onboarding.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.floosi.data.datastore.UserPreferences
import com.floosi.domain.model.Wallet
import com.floosi.domain.model.WalletType
import com.floosi.domain.repository.WalletRepository
import com.floosi.feature.onboarding.R
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
class OnboardingViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val walletRepository: WalletRepository,
    private val application: Application
) : ViewModel() {

    private val _state = MutableStateFlow(OnboardingState())
    val state: StateFlow<OnboardingState> = _state.asStateFlow()

    fun onIntent(intent: OnboardingIntent) {
        when (intent) {
            is OnboardingIntent.SetName -> _state.update { it.copy(userName = intent.name) }
            is OnboardingIntent.SetCurrency -> _state.update { it.copy(baseCurrency = intent.currency) }
            is OnboardingIntent.SetWalletName -> _state.update { it.copy(walletName = intent.name) }
            is OnboardingIntent.SetWalletType -> _state.update { it.copy(walletType = intent.type) }
            is OnboardingIntent.SetWalletBalance -> _state.update { it.copy(walletBalance = intent.balance) }
            is OnboardingIntent.NextStep -> _state.update { it.copy(step = it.step + 1) }
            is OnboardingIntent.PreviousStep -> _state.update { it.copy(step = it.step - 1) }
            is OnboardingIntent.Complete -> complete()
        }
    }

    fun saveProfile() {
        val current = _state.value
        viewModelScope.launch {
            userPreferences.setUserName(current.userName)
            userPreferences.setBaseCurrency(current.baseCurrency)
            _state.update { it.copy(step = it.step + 1) }
        }
    }

    fun saveWallet() {
        val current = _state.value
        viewModelScope.launch {
            val now = LocalDateTime.now()
            val wallet = Wallet(
                name = current.walletName.ifBlank { application.getString(R.string.onboarding_default_wallet) },
                type = current.walletType,
                currencyCode = current.baseCurrency,
                balance = current.walletBalance.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                initialBalance = current.walletBalance.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                colorArgb = 0xFF0F6E5C.toInt(),
                iconKey = current.walletType.iconKey,
                sortOrder = 0,
                createdAt = now,
                updatedAt = now
            )
            walletRepository.insert(wallet)
            userPreferences.setOnboardingComplete(true)
            _state.update { it.copy(step = it.step + 1) }
        }
    }

    private fun complete() {
        _state.update { it.copy(isComplete = true) }
    }
}
