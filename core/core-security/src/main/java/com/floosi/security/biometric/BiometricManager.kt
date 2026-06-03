package com.floosi.security.biometric

import android.content.Context
import androidx.biometric.BiometricManager as AndroidBiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BiometricManager @Inject constructor() {

    fun isBiometricAvailable(context: Context): Boolean {
        val manager = AndroidBiometricManager.from(context)
        return manager.canAuthenticate(
            AndroidBiometricManager.Authenticators.BIOMETRIC_STRONG
        ) == AndroidBiometricManager.BIOMETRIC_SUCCESS
    }

    fun authenticate(activity: FragmentActivity, callback: BiometricCallback) {
        val executor = Executors.newSingleThreadExecutor()
        val prompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    callback.onSuccess()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    callback.onError(errString.toString())
                }

                override fun onAuthenticationFailed() {
                    callback.onFailed()
                }
            }
        )

        val info = BiometricPrompt.PromptInfo.Builder()
            .setTitle("فتح فلوسي")
            .setSubtitle("استخدم بصمتك")
            .setAllowedAuthenticators(AndroidBiometricManager.Authenticators.BIOMETRIC_STRONG)
            .build()

        prompt.authenticate(info)
    }
}

interface BiometricCallback {
    fun onSuccess()
    fun onError(error: String)
    fun onFailed()
}
