package com.floosi.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.floosi.domain.model.ThemeMode
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.preferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferences @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    private val dataStore: DataStore<Preferences> = context.preferencesDataStore

    companion object {
        private val KEY_USER_NAME = stringPreferencesKey("user_name")
        private val KEY_BASE_CURRENCY = stringPreferencesKey("base_currency")
        private val KEY_THEME_MODE = stringPreferencesKey("theme_mode")
        private val KEY_IS_ONBOARDING_COMPLETE = booleanPreferencesKey("is_onboarding_complete")
        private val KEY_IS_LOCK_ENABLED = booleanPreferencesKey("is_lock_enabled")
    }

    val userName: Flow<String?> = dataStore.data.map { it[KEY_USER_NAME] }

    val baseCurrency: Flow<String> = dataStore.data.map { it[KEY_BASE_CURRENCY] ?: "EGP" }

    val themeMode: Flow<ThemeMode> = dataStore.data.map { preferences ->
        when (preferences[KEY_THEME_MODE]) {
            "LIGHT" -> ThemeMode.LIGHT
            "DARK" -> ThemeMode.DARK
            else -> ThemeMode.SYSTEM
        }
    }

    val isOnboardingComplete: Flow<Boolean> = dataStore.data.map {
        it[KEY_IS_ONBOARDING_COMPLETE] ?: false
    }

    val isLockEnabled: Flow<Boolean> = dataStore.data.map {
        it[KEY_IS_LOCK_ENABLED] ?: false
    }

    suspend fun setUserName(name: String) {
        dataStore.edit { it[KEY_USER_NAME] = name }
    }

    suspend fun setBaseCurrency(currency: String) {
        dataStore.edit { it[KEY_BASE_CURRENCY] = currency }
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        dataStore.edit { it[KEY_THEME_MODE] = mode.name }
    }

    suspend fun setOnboardingComplete(complete: Boolean) {
        dataStore.edit { it[KEY_IS_ONBOARDING_COMPLETE] = complete }
    }

    suspend fun setLockEnabled(enabled: Boolean) {
        dataStore.edit { it[KEY_IS_LOCK_ENABLED] = enabled }
    }

    suspend fun clear() {
        dataStore.edit { it.clear() }
    }
}
