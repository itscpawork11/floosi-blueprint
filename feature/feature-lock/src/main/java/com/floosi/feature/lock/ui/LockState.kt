package com.floosi.feature.lock.ui

data class LockState(
    val pin: String = "",
    val isError: Boolean = false,
    val errorMessageRes: Int? = null,
    val attempts: Int = 0,
    val isBiometricAvailable: Boolean = false,
    val isCooldown: Boolean = false,
    val cooldownRemaining: Int = 0
)

sealed interface LockIntent {
    data class AddDigit(val digit: Char) : LockIntent
    data object DeleteDigit : LockIntent
    data object ClearPin : LockIntent
    data object VerifyPin : LockIntent
    data object AuthenticateBiometric : LockIntent
}
