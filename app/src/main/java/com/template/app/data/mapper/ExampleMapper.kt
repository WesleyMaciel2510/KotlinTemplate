package com.template.app.data.mapper

import com.template.app.data.dto.ExampleItemDto
import com.template.app.domain.model.ExampleItem

fun ExampleItemDto.toDomain(): ExampleItem {
    return ExampleItem(
        id = id,
        title = title,
        description = description
    )
}