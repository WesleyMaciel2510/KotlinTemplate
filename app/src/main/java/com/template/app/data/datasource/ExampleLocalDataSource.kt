package com.template.app.data.datasource

import com.template.app.data.dto.ExampleItemDto
import javax.inject.Inject

class ExampleLocalDataSource @Inject constructor() {
    private val fakeData = listOf(
        ExampleItemDto(1, "Item 1", "Description for item 1"),
        ExampleItemDto(2, "Item 2", "Description for item 2"),
        ExampleItemDto(3, "Item 3", "Description for item 3"),
        ExampleItemDto(4, "Item 4", "Description for item 4"),
        ExampleItemDto(5, "Item 5", "Description for item 5")
    )

    suspend fun getItems(): List<ExampleItemDto> = fakeData
}