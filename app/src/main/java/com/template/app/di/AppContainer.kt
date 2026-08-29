package com.template.app.di

import com.template.app.data.datasource.ExampleLocalDataSource
import com.template.app.data.repository.ExampleRepositoryImpl
import com.template.app.domain.repository.ExampleRepository
import com.template.app.domain.usecase.GetExampleItemsUseCase
import com.template.app.presentation.home.HomeViewModel

class AppContainer {
    // Data layer
    val exampleLocalDataSource: ExampleLocalDataSource = ExampleLocalDataSource()
    val exampleRepository: ExampleRepository = ExampleRepositoryImpl(
        localDataSource = exampleLocalDataSource
    )

    // Domain layer
    val getExampleItemsUseCase: GetExampleItemsUseCase = GetExampleItemsUseCase(
        repository = exampleRepository
    )

    // Presentation layer - ViewModel factory
    fun createHomeViewModel(): HomeViewModel {
        return HomeViewModel(
            getExampleItemsUseCase = getExampleItemsUseCase
        )
    }
}