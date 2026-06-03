package com.floosi.feature.onboarding.ui

import com.floosi.domain.model.WalletType

data class OnboardingState(
    val step: Int = 0,
    val userName: String = "",
    val baseCurrency: String = "EGP",
    val walletName: String = "",
    val walletType: WalletType = WalletType.CASH,
    val walletBalance: String = "",
    val isComplete: Boolean = false
)

sealed interface OnboardingIntent {
    data class SetName(val name: String) : OnboardingIntent
    data class SetCurrency(val currency: String) : OnboardingIntent
    data class SetWalletName(val name: String) : OnboardingIntent
    data class SetWalletType(val type: WalletType) : OnboardingIntent
    data class SetWalletBalance(val balance: String) : OnboardingIntent
    data object NextStep : OnboardingIntent
    data object PreviousStep : OnboardingIntent
    data object Complete : OnboardingIntent
}
