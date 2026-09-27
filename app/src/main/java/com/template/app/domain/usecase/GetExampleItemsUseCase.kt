package com.template.app.domain.usecase

import com.template.app.domain.model.ExampleItem
import com.template.app.domain.repository.ExampleRepository
import javax.inject.Inject

class GetExampleItemsUseCase @Inject constructor(
    private val repository: ExampleRepository
) {
    suspend operator fun invoke(): Result<List<ExampleItem>> = repository.getItems()
}