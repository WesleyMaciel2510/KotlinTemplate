package com.template.app.di

import com.template.app.data.remote.ApiService
import com.template.app.data.remote.ApiServiceImpl
import com.template.app.data.repository.ExampleRepositoryImpl
import com.template.app.data.repository.ItemRepository
import com.template.app.data.repository.ItemRepositoryImpl
import com.template.app.domain.repository.ExampleRepository
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
}
