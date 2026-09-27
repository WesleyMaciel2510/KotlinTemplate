package com.template.app.data.mapper

import com.template.app.data.dto.ExampleItemDto
import com.template.app.domain.model.ExampleItem
import javax.inject.Inject

class ExampleMapper @Inject constructor() {
    fun toDomain(dto: ExampleItemDto): ExampleItem = ExampleItem(
        id = dto.id,
        title = dto.title,
        description = dto.description
    )
}