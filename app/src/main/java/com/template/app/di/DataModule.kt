package com.template.app.di

import com.template.app.data.remote.ApiService
import com.template.app.data.remote.ApiServiceImpl
import com.template.app.data.repository.ExampleRepositoryImpl
import com.template.app.data.repository.FakeFinanceRepository
import com.template.app.data.repository.ItemRepository
import com.template.app.data.repository.ItemRepositoryImpl
import com.template.app.domain.repository.ExampleRepository
import com.template.app.domain.repository.FinanceRepository
import com.template.app.domain.repository.RecebiveisRepository
import com.template.app.data.repository.FakeRecebiveisRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindApiService(
        impl: ApiServiceImpl
    ): ApiService

    @Binds
    @Singleton
    abstract fun bindItemRepository(
        impl: ItemRepositoryImpl
    ): ItemRepository

    @Binds
    @Singleton
    abstract fun bindExampleRepository(
        impl: ExampleRepositoryImpl
    ): ExampleRepository

    @Binds
    @Singleton
    abstract fun bindFinanceRepository(
        impl: FakeFinanceRepository
    ): FinanceRepository

    @Binds
    @Singleton
    abstract fun bindRecebiveisRepository(
        impl: FakeRecebiveisRepository
    ): RecebiveisRepository
}
