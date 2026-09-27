package com.template.app.domain.repository

import com.template.app.domain.model.ExampleItem

interface ExampleRepository {
    suspend fun getItems(): Result<List<ExampleItem>>
}
