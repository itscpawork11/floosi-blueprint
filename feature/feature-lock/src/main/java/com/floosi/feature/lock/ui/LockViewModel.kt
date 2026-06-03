package com.floosi.feature.lock.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.floosi.feature.lock.R
import com.floosi.security.biometric.BiometricManager
import com.floosi.security.crypto.EncryptionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LockViewModel @Inject constructor(
    private val encryptionManager: EncryptionManager,
    private val biometricManager: BiometricManager,
    private val application: Application
) : ViewModel() {

    private val _state = MutableStateFlow(LockState())
    val state: StateFlow<LockState> = _state.asStateFlow()

    private var cooldownJob: Job? = null
    private val pinLength = 4

    val isPinSet: Boolean get() = encryptionManager.isPinSet()

    init {
        _state.update {
            it.copy(
                isBiometricAvailable = biometricManager.isBiometricAvailable(application)
            )
        }
    }

    fun onIntent(intent: LockIntent) {
        when (intent) {
            is LockIntent.AddDigit -> addDigit(intent.digit)
            is LockIntent.DeleteDigit -> deleteDigit()
            is LockIntent.ClearPin -> clearPin()
            is LockIntent.VerifyPin -> verifyPin()
            is LockIntent.AuthenticateBiometric -> authenticateBiometric()
        }
    }

    private fun addDigit(digit: Char) {
        if (_state.value.isCooldown) return
        val current = _state.value.pin
        if (current.length >= pinLength) return
        _state.update { it.copy(pin = current + digit, isError = false) }
    }

    private fun deleteDigit() {
        if (_state.value.pin.isEmpty()) return
        _state.update { it.copy(pin = it.pin.dropLast(1)) }
    }

    private fun clearPin() {
        _state.update { it.copy(pin = "", isError = false, errorMessageRes = null) }
    }

    private fun verifyPin() {
        val current = _state.value
        if (current.pin.length != pinLength) return

        val isValid = encryptionManager.verifyPin(current.pin)
        if (isValid) {
            _state.update { it.copy(pin = "", isError = false, errorMessageRes = null, attempts = 0) }
            onUnlocked()
        } else {
            val newAttempts = current.attempts + 1
            _state.update {
                it.copy(
                    pin = "",
                    isError = true,
                    errorMessageRes = R.string.lock_wrong_pin,
                    attempts = newAttempts
                )
            }
            if (newAttempts >= 5) {
                startCooldown()
            }
        }
    }

    private fun startCooldown() {
        cooldownJob?.cancel()
        cooldownJob = viewModelScope.launch {
            val durations = listOf(10, 60)
            val cooldownSeconds = if (_state.value.attempts < 10) durations[0] else durations[1]
            _state.update { it.copy(isCooldown = true, cooldownRemaining = cooldownSeconds) }

            for (i in cooldownSeconds downTo 1) {
                _state.update { it.copy(cooldownRemaining = i) }
                delay(1000)
            }
            _state.update { it.copy(isCooldown = false, cooldownRemaining = 0) }
        }
    }

    private fun authenticateBiometric() {
        onBiometricAuthRequested()
    }

    private var onUnlocked: () -> Unit = {}
    private var onBiometricAuthRequested: () -> Unit = {}

    fun setOnUnlocked(callback: () -> Unit) {
        onUnlocked = callback
    }

    fun setOnBiometricAuthRequested(callback: () -> Unit) {
        onBiometricAuthRequested = callback
    }
}
