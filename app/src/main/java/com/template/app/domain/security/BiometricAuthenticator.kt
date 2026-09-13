package com.template.app.domain.security

interface BiometricAuthenticator {
    suspend fun isAvailable(): Boolean
    suspend fun authenticate(): BiometricAuthResult
}
