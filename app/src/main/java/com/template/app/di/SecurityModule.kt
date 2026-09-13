package com.template.app.di

import android.content.Context
import com.template.app.data.security.AndroidBiometricAuthenticator
import com.template.app.data.security.EncryptedSecurityPreferences
import com.template.app.domain.security.BiometricAuthenticator
import com.template.app.data.security.SecurityPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SecurityModule {

    @Provides
    @Singleton
    fun provideBiometricAuthenticator(
        @ApplicationContext context: Context
    ): BiometricAuthenticator {
        return AndroidBiometricAuthenticator(context)
    }

    @Provides
    @Singleton
    fun provideSecurityPreferences(
        @ApplicationContext context: Context
    ): SecurityPreferences {
        return EncryptedSecurityPreferences(context)
    }
}
