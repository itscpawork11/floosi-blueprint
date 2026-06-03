package com.floosi.feature.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.floosi.data.datastore.UserPreferences
import com.floosi.domain.model.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                userPreferences.userName,
                userPreferences.baseCurrency,
                userPreferences.themeMode,
                userPreferences.isLockEnabled,
            ) { name, currency, theme, lock ->
                SettingsState(
                    userName = name ?: "",
                    baseCurrency = currency,
                    themeMode = theme,
                    isLockEnabled = lock,
                    isLoading = false,
                )
            }.collect { settings ->
                _state.value = settings
            }
        }
    }

    fun onIntent(intent: SettingsIntent) {
        viewModelScope.launch {
            when (intent) {
                is SettingsIntent.SetUserName -> userPreferences.setUserName(intent.name)
                is SettingsIntent.SetBaseCurrency -> userPreferences.setBaseCurrency(intent.currency)
                is SettingsIntent.SetThemeMode -> userPreferences.setThemeMode(intent.mode)
                is SettingsIntent.SetLockEnabled -> userPreferences.setLockEnabled(intent.enabled)
                SettingsIntent.ClearAllData -> userPreferences.clear()
            }
        }
    }
}
