package com.template.app.data.datasource

import com.template.app.data.dto.ExampleItemDto
import kotlinx.coroutines.delay

class ExampleLocalDataSource {
    suspend fun getItems(): List<ExampleItemDto> {
        delay(500) // Simulate network/disk delay
        return listOf(
            ExampleItemDto(1, "First Item", "This is the first example item"),
            ExampleItemDto(2, "Second Item", "This is the second example item"),
            ExampleItemDto(3, "Third Item", "This is the third example item"),
            ExampleItemDto(4, "Fourth Item", "This is the fourth example item"),
            ExampleItemDto(5, "Fifth Item", "This is the fifth example item")
        )
    }
}