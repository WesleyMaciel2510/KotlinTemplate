package com.template.app.data.security

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.template.app.domain.security.BiometricAuthResult
import com.template.app.domain.security.BiometricAuthenticator
import com.template.app.domain.security.UnavailableReason
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class AndroidBiometricAuthenticator @Inject constructor(
    private val context: Context
) : BiometricAuthenticator {

    override suspend fun isAvailable(): Boolean {
        val biometricManager = BiometricManager.from(context)
        val result = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)
        return result == BiometricManager.BIOMETRIC_SUCCESS
    }

    override suspend fun authenticate(): BiometricAuthResult = suspendCancellableCoroutine { continuation ->
        val activity = context as? FragmentActivity
        if (activity == null) {
            continuation.resume(BiometricAuthResult.Error("Context is not a FragmentActivity"))
            return@suspendCancellableCoroutine
        }

        val executor = ContextCompat.getMainExecutor(activity)
        val biometricPrompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    if (continuation.isActive) {
                        continuation.resume(BiometricAuthResult.Success)
                    }
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    if (!continuation.isActive) return
                    val authResult = when (errorCode) {
                        BiometricPrompt.ERROR_USER_CANCELED,
                        BiometricPrompt.ERROR_NEGATIVE_BUTTON -> BiometricAuthResult.Canceled
                        11 -> BiometricAuthResult.NotEnrolled // ERROR_NONE_ENROLLED
                        9 -> BiometricAuthResult.LockedOut // ERROR_LOCKOUT
                        10 -> BiometricAuthResult.PermanentLockout // ERROR_LOCKOUT_PERMANENT
                        1, 12 -> BiometricAuthResult.NotAvailable(UnavailableReason.NoHardware) // ERROR_HW_UNAVAILABLE / ERROR_NO_HARDWARE
                        else -> BiometricAuthResult.Error(errString.toString())
                    }
                    continuation.resume(authResult)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                }
            }
        )

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autenticação por impressão digital")
            .setSubtitle("Confirme sua identidade para continuar")
            .setNegativeButtonText("Cancelar")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .build()

        try {
            biometricPrompt.authenticate(promptInfo)
        } catch (e: Exception) {
            if (continuation.isActive) {
                continuation.resume(BiometricAuthResult.Error(e.localizedMessage ?: "Unknown exception"))
            }
        }
    }
}
