package com.floosi.feature.settings.ui

import com.floosi.domain.model.ThemeMode

data class SettingsState(
    val userName: String = "",
    val baseCurrency: String = "EGP",
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val isLockEnabled: Boolean = false,
    val isLoading: Boolean = true,
)

sealed interface SettingsIntent {
    data class SetUserName(val name: String) : SettingsIntent
    data class SetBaseCurrency(val currency: String) : SettingsIntent
    data class SetThemeMode(val mode: ThemeMode) : SettingsIntent
    data class SetLockEnabled(val enabled: Boolean) : SettingsIntent
    data object ClearAllData : SettingsIntent
}
