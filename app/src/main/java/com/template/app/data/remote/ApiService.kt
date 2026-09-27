package com.template.app.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject
import javax.inject.Singleton

interface ApiService {
    suspend fun getItems(): List<ItemDto>
    suspend fun getItemById(id: String): ItemDto
    suspend fun createItem(item: ItemDto): ItemDto
}

@Singleton
class ApiServiceImpl @Inject constructor(
    private val client: HttpClient
) : ApiService {

    companion object {
        private const val BASE_URL = "https://api.example.com"
    }

    override suspend fun getItems(): List<ItemDto> {
        return client.get("$BASE_URL/items").body()
    }

    override suspend fun getItemById(id: String): ItemDto {
        return client.get("$BASE_URL/items/$id").body()
    }

    override suspend fun createItem(item: ItemDto): ItemDto {
        return client.post("$BASE_URL/items") {
            contentType(ContentType.Application.Json)
            setBody(item)
        }.body()
    }
}
