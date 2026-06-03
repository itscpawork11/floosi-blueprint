package com.floosi.data.datastore

import com.floosi.domain.model.ThemeMode
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Serializable
data class SettingsData(
    val userName: String? = null,
    val baseCurrency: String = "EGP",
    val themeMode: String = "SYSTEM",
    val isOnboardingComplete: Boolean = false,
    val isLockEnabled: Boolean = false,
)

@Singleton
class SettingsSerializer @Inject constructor(
    private val userPreferences: UserPreferences,
) {

    private val json = Json { ignoreUnknownKeys = true }

    suspend fun exportToJson(): String {
        val userName: String? = userPreferences.userName.firstOrNull()
        val baseCurrency = userPreferences.baseCurrency.first()
        val themeMode = userPreferences.themeMode.first().name
        val isOnboardingComplete = userPreferences.isOnboardingComplete.first()
        val isLockEnabled = userPreferences.isLockEnabled.first()

        val settings = SettingsData(
            userName = userName,
            baseCurrency = baseCurrency,
            themeMode = themeMode,
            isOnboardingComplete = isOnboardingComplete,
            isLockEnabled = isLockEnabled,
        )
        return json.encodeToString(settings)
    }

    suspend fun importFromJson(jsonString: String) {
        val settings = json.decodeFromString<SettingsData>(jsonString)
        settings.userName?.let { userPreferences.setUserName(it) }
        userPreferences.setBaseCurrency(settings.baseCurrency)
        userPreferences.setThemeMode(ThemeMode.valueOf(settings.themeMode))
        userPreferences.setOnboardingComplete(settings.isOnboardingComplete)
        userPreferences.setLockEnabled(settings.isLockEnabled)
    }
}
