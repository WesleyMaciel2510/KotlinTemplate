package com.template.app.data.repository

import com.template.app.data.datasource.ExampleLocalDataSource
import com.template.app.data.mapper.toDomain
import com.template.app.domain.model.ExampleItem
import com.template.app.domain.repository.ExampleRepository
import kotlin.Result

class ExampleRepositoryImpl(
    private val localDataSource: ExampleLocalDataSource
) : ExampleRepository {
    override suspend fun getItems(): Result<List<ExampleItem>> {
        return try {
            val dtos = localDataSource.getItems()
            val items = dtos.map { it.toDomain() }
            Result.success(items)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}