package com.template.app.data.remote

import javax.inject.Inject
import javax.inject.Singleton

sealed interface NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>
    data class Error(val exception: Throwable, val message: String? = exception.message) : NetworkResult<Nothing>
}

@Singleton
class RemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun fetchItems(): NetworkResult<List<ItemDto>> {
        return runCatching {
            apiService.getItems()
        }.fold(
            onSuccess = { NetworkResult.Success(it) },
            onFailure = { NetworkResult.Error(it) }
        )
    }

    suspend fun fetchItemById(id: String): NetworkResult<ItemDto> {
        return runCatching {
            apiService.getItemById(id)
        }.fold(
            onSuccess = { NetworkResult.Success(it) },
            onFailure = { NetworkResult.Error(it) }
        )
    }

    suspend fun createItem(item: ItemDto): NetworkResult<ItemDto> {
        return runCatching {
            apiService.createItem(item)
        }.fold(
            onSuccess = { NetworkResult.Success(it) },
            onFailure = { NetworkResult.Error(it) }
        )
    }
}
