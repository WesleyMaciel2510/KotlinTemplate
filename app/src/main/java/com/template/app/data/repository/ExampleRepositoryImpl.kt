package com.template.app.data.repository

import com.template.app.data.datasource.ExampleLocalDataSource
import com.template.app.data.mapper.ExampleMapper
import com.template.app.domain.model.ExampleItem
import com.template.app.domain.repository.ExampleRepository
import javax.inject.Inject

class ExampleRepositoryImpl @Inject constructor(
    private val localDataSource: ExampleLocalDataSource,
    private val mapper: ExampleMapper
) : ExampleRepository {
    override suspend fun getItems(): Result<List<ExampleItem>> {
        return try {
            val dtos = localDataSource.getItems()
            val items = dtos.map { mapper.toDomain(it) }
            Result.success(items)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}