package com.template.app.domain.usecase

import com.template.app.domain.model.ExampleItem
import com.template.app.domain.repository.ExampleRepository
import kotlinx.coroutines.runBlocking
import kotlin.Result

class GetExampleItemsUseCase(
    private val repository: ExampleRepository
) {
    operator fun invoke(): Result<List<ExampleItem>> {
        return runBlocking {
            repository.getItems()
        }
    }
}