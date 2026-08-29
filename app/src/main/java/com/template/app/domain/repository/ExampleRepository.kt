package com.template.app.domain.repository

import com.template.app.domain.model.ExampleItem
import kotlin.Result

interface ExampleRepository {
    suspend fun getItems(): Result<List<ExampleItem>>
}