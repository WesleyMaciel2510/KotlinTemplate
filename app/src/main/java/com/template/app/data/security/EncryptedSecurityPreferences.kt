package com.template.app.data.security

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import javax.inject.Inject

interface SecurityPreferences {
    var biometricLoginEnabled: Boolean
}

class EncryptedSecurityPreferences @Inject constructor(
    private val context: Context
) : SecurityPreferences {

    private val sharedPreferences by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            context,
            "secure_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    override var biometricLoginEnabled: Boolean
        get() = sharedPreferences.getBoolean("biometric_login_enabled", false)
        set(value) = sharedPreferences.edit().putBoolean("biometric_login_enabled", value).apply()
}
