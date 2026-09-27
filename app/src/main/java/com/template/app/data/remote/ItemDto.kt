package com.template.app.data.remote

import com.template.app.data.local.ItemEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemDto(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String? = null,
    @SerialName("imageUrl") val imageUrl: String? = null
)

fun ItemDto.toEntity(): ItemEntity {
    return ItemEntity(
        id = id,
        title = title,
        description = description ?: "",
        imageUrl = imageUrl ?: ""
    )
}

fun ItemEntity.toDto(): ItemDto {
    return ItemDto(
        id = id,
        title = title,
        description = description,
        imageUrl = imageUrl
    )
}
