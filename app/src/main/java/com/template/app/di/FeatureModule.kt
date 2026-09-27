package com.template.app.di

import com.template.app.financeiro.data.FakeFinanceiroRepository
import com.template.app.financeiro.data.FinanceiroRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FeatureModule {
    @Provides
    @Singleton
    fun provideFinanceiroRepository(): FinanceiroRepository = FakeFinanceiroRepository()
}
