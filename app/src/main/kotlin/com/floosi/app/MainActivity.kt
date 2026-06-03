package com.floosi.app

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.first
import com.floosi.app.navigation.AppNavigation
import com.floosi.core.designsystem.FloosiTheme
import com.floosi.data.datastore.UserPreferences
import com.floosi.domain.model.ThemeMode
import com.floosi.security.crypto.EncryptionManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var userPreferences: UserPreferences

    @Inject lateinit var encryptionManager: EncryptionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val themeMode by userPreferences.themeMode.collectAsStateWithLifecycle(ThemeMode.SYSTEM)
            val isLockEnabled by userPreferences.isLockEnabled.collectAsStateWithLifecycle(false)

            var startRoute by remember { mutableStateOf<String?>(null) }

            LaunchedEffect(Unit) {
                val isOnboardingComplete = userPreferences.isOnboardingComplete.first()
                val hasPin = encryptionManager.isPinSet()
                startRoute = when {
                    !isOnboardingComplete -> com.floosi.app.navigation.Routes.ONBOARDING
                    isLockEnabled && hasPin -> com.floosi.app.navigation.Routes.LOCK
                    else -> com.floosi.app.navigation.Routes.HOME
                }
            }

            LaunchedEffect(isLockEnabled) {
                if (isLockEnabled) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
                } else {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
                }
            }

            val darkTheme = when (themeMode) {
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
            }

            FloosiTheme(darkTheme = darkTheme) {
                startRoute?.let { route ->
                    Surface(modifier = Modifier.fillMaxSize()) {
                        AppNavigation(startRoute = route)
                    }
                }
            }
        }
    }
}
