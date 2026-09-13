package com.template.app.domain.security

sealed interface BiometricAuthResult {
    data object Success : BiometricAuthResult
    data object Canceled : BiometricAuthResult
    data object NotEnrolled : BiometricAuthResult
    data object LockedOut : BiometricAuthResult
    data object PermanentLockout : BiometricAuthResult
    data class NotAvailable(val reason: UnavailableReason) : BiometricAuthResult
    data class Error(val message: String) : BiometricAuthResult
}

enum class UnavailableReason {
    NoHardware,
    HardwareUnavailable,
    DevicePolicy,
    Unknown
}
